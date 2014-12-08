package cn.gotom.sso.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.util.Date;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class SignCert
{
	public static void main(String args[]) throws Exception
	{
		String storePath = "D:/Apache/tomcat-7.0.57/keystore/";
		char[] password = "888888".toCharArray();
		String alias = "gotom.cn";
		String keyStoreFile = storePath + "keystore.jks";
		String truststoreFile = storePath + "truststore.jks";
		// Cert of CA-----c1
		FileInputStream in = new FileInputStream(keyStoreFile);
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(in, password);
		java.security.cert.Certificate c1 = ks.getCertificate(alias);
		PrivateKey caprk = (PrivateKey) ks.getKey(alias, password);
		in.close();
		// 得到签发者
		X509CertImpl cimp1 = new X509CertImpl(c1.getEncoded());
		X509CertInfo cinfo1 = (X509CertInfo) cimp1.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
		X500Name issuer = (X500Name) cinfo1.get(X509CertInfo.SUBJECT + "." + CertificateIssuerName.DN_NAME);
		// Cert of lf-----c2
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in2 = new FileInputStream(truststoreFile);
		java.security.cert.Certificate c2 = cf.generateCertificate(in2);
		in2.close();
		byte[] encod2 = c2.getEncoded();
		X509CertImpl cimp2 = new X509CertImpl(encod2);
		X509CertInfo cinfo2 = (X509CertInfo) cimp2.get(X509CertImpl.NAME + "." + X509CertImpl.INFO);
		// 设置新证书有效期
		Date begindate = new Date();
		// 60 day
		Date enddate = new Date(begindate.getTime() + 3000 * 24 * 60 * 60 * 1000L);
		CertificateValidity cv = new CertificateValidity(begindate, enddate);
		cinfo2.set(X509CertInfo.VALIDITY, cv);
		// 设置新证书序列号
		int sn = (int) (begindate.getTime() / 1000);
		CertificateSerialNumber csn = new CertificateSerialNumber(sn);
		cinfo2.set(X509CertInfo.SERIAL_NUMBER, csn);
		// 设置新证书签发者
		cinfo2.set(X509CertInfo.ISSUER + "." + CertificateIssuerName.DN_NAME, issuer);
		// 设置新证书算法
		AlgorithmId algorithm = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
		cinfo2.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algorithm);
		// 创建证书
		X509CertImpl newcert = new X509CertImpl(cinfo2);
		// 签名
		newcert.sign(caprk, "MD5WithRSA");
		System.out.println(newcert);
		// 存入密钥库
		ks.setCertificateEntry("lf_signed", newcert);
		/*
		 * PrivateKey prk=(PrivateKey)ks.getKey("lf", "wshr.ut".toCharArray( )); java.security.cert.Certificate[] cchain={newcert}; ks.setKeyEntry("lf_signed",prk, "newpass".toCharArray(),cchain);
		 */
		FileOutputStream out = new FileOutputStream("newstore");
		ks.store(out, "newpass".toCharArray());
		out.close();
	}
}
