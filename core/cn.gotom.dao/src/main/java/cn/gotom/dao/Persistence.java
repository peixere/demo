package cn.gotom.dao;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.ejb.packaging.NativeScanner;
import org.hibernate.ejb.packaging.Scanner;

import cn.gotom.matcher.UrlMatcher;
import cn.gotom.matcher.UrlMatcherAnt;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

@Singleton
public class Persistence implements PersistenceLifeCycle
{

	private final UnitOfWork unitOfWork;
	private final PersistService persistService;
	private String pattern = "*.pojos.*";

	private final UrlMatcher urlMatcher = new UrlMatcherAnt();
	
	@Inject
	public Persistence(UnitOfWork unitOfWork, PersistService persistService)
	{
		this.unitOfWork = unitOfWork;
		this.persistService = persistService;
	}
	@Override
	public void startService()
	{
		try
		{
			Set<Class<?>> matchingClasses = scanPersistentClass();
			persistService.addAnnotatedClasses(matchingClasses);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		this.persistService.start();
	}

	private Set<Class<?>> scanPersistentClass()
	{
		String path = getJarPath();
		Set<Class<?>> matchingClasses = new HashSet<Class<?>>();
		try
		{
			Scanner scanner = new NativeScanner();
			Set<Class<? extends Annotation>> annotationsToExclude = new HashSet<Class<? extends Annotation>>(3);
			annotationsToExclude.add(Entity.class);
			annotationsToExclude.add(MappedSuperclass.class);
			annotationsToExclude.add(Embeddable.class);
			File file = new File(path);
			File[] files = file.listFiles();
			for (File f : files)
			{
				try
				{
					if (urlMatcher.pathMatchesUrl(pattern, f.getName()))
					{
						URL url = f.toURI().toURL();
						matchingClasses.addAll(scanner.getClassesInJar(url, annotationsToExclude));
					}
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return matchingClasses;
	}

	private String getJarPath()
	{
		String domainPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		File file = new File(domainPath);
		if (!file.isDirectory())
		{
			domainPath = domainPath.substring(0, domainPath.length() - file.getName().length());
		}
		return domainPath;
	}
	@Override
	public void stopService()
	{
		this.persistService.stop();
	}
	@Override
	public void beginUnitOfWork()
	{
		this.unitOfWork.begin();
	}
	@Override
	public void endUnitOfWork()
	{
		this.unitOfWork.end();
	}
	@Override
	public String getPattern()
	{
		return pattern;
	}
	@Override
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}
	
	
}
