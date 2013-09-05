package javax.comm;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.sun.comm.CommProperties;
import com.sun.comm.PlatformPortBundle;
import com.sun.comm.Portmapping;

public class CommPortIdentifier
{
	public static final int PORT_SERIAL = 1;
	public static final int PORT_PARALLEL = 2;
	private int portType;
	private static CommDriver commDriver = null;
	private static String portmapFilename;
	private static String propFilename;
	private static Object lock = new Object();
	private CommPort port;
	private Vector<CommPortOwnershipListener> listeners;
	String name;
	String owner;
	boolean owned;
	boolean waitingForPortAvailablity;
	String copyright_notice = "ENGLISH COPYRIGHT\nCopyright (c) 2004 Sun Microsystems, Inc.,4150 Network Circle, Santa Clara, California95054, U.S.A. All rights reserved.\n\nU.S. Government Rights - Commercial software. Government users are subject to the Sun\nMicrosystems, Inc. standard license agreement and applicable provisions of the FAR and \nits supplements.\n\nUse is subject to license terms.\n\nParts of the product may be derived from Berkeley BSD systems, licensed from the Univ\nersity of California. UNIX is a registered trademark in the U.S. and in other countri\nes, exclusively licensed through X/Open Company, Ltd.\n\nSun, Sun Microsystems, the Sun logo, Java and Jini are trademarks or registered trade\nmarks of Sun Microsystems, Inc. in the U.S. and other countries.\n\n\nFRENCH COPYRIGHT:\nCopyright <A9> 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara, Califor\nnia 95054, Etats-Unis. Tous droits rservs.\n\nL'utilisation est soumise aux termes de la Licence.\n\nDes parties de ce produit pourront tre drives des systmes Berkeley BS\nD licenci<E9>s par l'Universit<E9> de Californie. UNIX est une marque dpose a\nux Etats-Unis et dans d'autres pays et licenci<E9>e exclusivement par X/Open Company,\nLtd.\n\nSun, Sun Microsystems, le logo Sun, Java et Jini sont des marques de fabrique ou des \nmarques dposes de Sun Microsystems, Inc. aux Etats-Unis et dans d'autres pays\n";

	String license = "Sun Microsystems, Inc.\nBinary Code License Agreement\n\nREAD THE TERMS OF THIS AGREEMENT AND ANY PROVIDED SUPPLEMENTAL LICENSE TERMS (COLLECT\nIVELY \"AGREEMENT\") CAREFULLY BEFORE OPENING THE SOFTWARE MEDIA PACKAGE.  BY OPENIN\nG THE SOFTWARE MEDIA PACKAGE, YOU AGREE TO THE TERMS OF THIS AGREEMENT.  IF YOU AR\nE ACCESSING THE SOFTWARE ELECTRONICALLY, INDICATE YOUR ACCEPTANCE OF THESE TERMS BY S\nELECTING THE \"ACCEPT\" BUTTON AT THE END OF THIS AGREEMENT.  IF YOU DO NOT AGREE TO\n ALL THESE TERMS, PROMPTLY RETURN THE UNUSED SOFTWARE TO YOUR PLACE OF PURCHASE FOR A\n REFUND OR, IF THE SOFTWARE IS ACCESSED ELECTRONICALLY, SELECT THE \"DECLINE\" BUTTON A\nT THE END OF THIS AGREEMENT. \n\n\n\n1.  LICENSE TO USE.  Sun grants you a non-exclusive and non-transferable licens\ne for the internal use only of the accompanying software and documentation and any er\nror corrections provided by Sun (collectively \"Software\"), by the number of users and\n the class of computer hardware for which the corresponding fee has been paid. \n\n\n\n2.  RESTRICTIONS.  Software is confidential and copyrighted. Title to Software\nand all associated intellectual property rights is retained by Sun and/or its licenso\nrs.  Except as specifically authorized in any Supplemental License Terms, you may\nnot make copies of Software, other than a single copy of Software for archival purpos\nes.  Unless enforcement is prohibited by applicable law, you may not modify, decom\npile, or reverse engineer Software.  Licensee acknowledges that Licensed Software\nis not designed or intended for use in the design, construction, operation or mainten\nance of any nuclear facility. Sun Microsystems, Inc. disclaims any express or implied\n warranty of fitness for such uses.   No right, title or interest in or to any tra\ndemark, service mark, logo or trade name of Sun or its licensors is granted under thi\ns Agreement. \n\n\n\n3.  LIMITED WARRANTY.  Sun warrants to you that for a period of ninety (90) days f\nrom the date of purchase, as evidenced by a copy of the receipt, the media on which S\noftware is furnished (if any) will be free of defects in materials and workmanship un\nder normal use.  Except for the foregoing, Software is provided \"AS IS\".  Your\nexclusive remedy and Sun's entire liability under this limited warranty will be at Su\nn's option to replace Software media or refund the fee paid for Software. \n\n\n\n4.  DISCLAIMER OF WARRANTY.  UNLESS SPECIFIED IN THIS AGREEMENT, ALL EXPRESS OR\n IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY O\nF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT ARE DISCLAIME\nD, EXCEPT TO THE EXTENT THAT THESE DISCLAIMERS ARE HELD TO BE LEGALLY INVALID. \n\n\n\n5.  LIMITATION OF LIABILITY.  TO THE EXTENT NOT PROHIBITED BY LAW, IN NO EVENT\nWILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR SPEC\nIAL, INDIRECT, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED REGARDLE\nSS OF THE THEORY OF LIABILITY, ARISING OUT OF OR RELATED TO THE USE OF OR INABILITY T\nO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. \nIn no event will Sun's liability to you, whether in contract, tort (including neglige\nnce), or otherwise, exceed the amount paid by you for Software under this Agreement.\\n240 The foregoing limitations will apply even if the above stated warranty fails of i\nts essential purpose. \n\n\n\n6.  Termination.  This Agreement is effective until terminated.  You may ter\nminate this Agreement at any time by destroying all copies of Software.  This Agre\nement will terminate immediately without notice from Sun if you fail to comply with a\nny provision of this Agreement.  Upon Termination, you must destroy all copies of\nSoftware. \n\n\n\n7.  Export Regulations. All Software and technical data delivered under this Agreemen\nt are subject to US export control laws and may be subject to export or import regula\ntions in other countries.  You agree to comply strictly with all such laws and reg\nulations and acknowledge that you have the responsibility to obtain such licenses to\nexport, re-export, or import as may be required after delivery to you. \n\n\n\n8.   U.S. Government Restricted Rights.  If Software is being acquired by or\n on behalf of the U.S. Government or by a U.S. Government prime contractor or subcont\nractor (at any tier), then the Government's rights in Software and accompanying docum\nentation will be only as set forth in this Agreement; this is in accordance with 48 C\nFR 227.7201 through 227.7202-4 (for Department of Defense (DOD) acquisitions) and wit\nh 48 CFR 2.101 and 12.212 (for non-DOD acquisitions). \n\n\n\n9.   Governing Law.  Any action related to this Agreement will be governed by C\nalifornia law and controlling U.S. federal law.  No choice of law rules of any jur\nisdiction will apply. \n\n\n\n10.  Severability. If any provision of this Agreement is held to be unenforceable,\n this Agreement will remain in effect with the provision omitted, unless omission wou\nld frustrate the intent of the parties, in which case this Agreement will immediately\n terminate. \n\n\n\n11.  Integration.  This Agreement is the entire agreement between you and Sun r\nelating to its subject matter.  It supersedes all prior or contemporaneous oral or\n written communications, proposals, representations and warranties and prevails over\nany conflicting or additional terms of any quote, order, acknowledgment, or other com\nmunication between the parties relating to its subject matter during the term of this\n Agreement.  No modification of this Agreement will be binding, unless in writing\nand signed by an authorized representative of each party. \n\n\n\nJAVA OPTIONAL PACKAGE \n\n\n\nJAVAX.COMM 3.0\n\n\n\nSUPPLEMENTAL LICENSE TERMS\n\n\n\nThese supplemental license terms (\"Supplemental Terms\") add to or modify the terms of\n the Binary Code License Agreement (collectively, the \"Agreement\"). Capitalized terms\n not defined in these Supplemental Terms shall have the same meanings ascribed to the\nm in the Agreement. These Supplemental Terms shall supersede any inconsistent or conf\nlicting terms in the Agreement, or in any license contained within the Software. \n\n\n                                                                                 \n1. Software Internal Use and Development License Grant.  Subject to the terms and\nconditions of this Agreement, including, but not limited to Section 3 (Java(TM) Techn\nology Restrictions) of these Supplemental Terms, Sun grants you a non-exclusive, non-\ntransferable, limited license to reproduce internally and use internally the binary f\norm of the Software, complete and unmodified, for the sole purpose of designing, deve\nloping and testing your Java applets and applications (\"Programs\"). \n\n\n\n2. License to Distribute Software.  In addition to the license granted in Section\n1 (Software Internal Use and Development License Grant) of these Supplemental Terms,\nsubject to the terms and conditions of this Agreement, including but not limited to,\nSection 3 (Java Technology Restrictions) of these Supplemental Terms, Sun grants you\na non-exclusive, non-transferable, limited license to reproduce and distribute the So\nftware in binary code form only, provided that you (i) distribute the Software comple\nte and unmodified and only bundled as part of your Programs, (ii) do not distribute a\ndditional software intended to replace any component(s) of the Software, (iii) do not\n remove or alter any proprietary legends or notices contained in the Software, (iv) o\nnly distribute the Software subject to a license agreement that protects Sun's intere\nsts consistent with the terms contained in this Agreement, and (v) agree to defend an\nd indemnify Sun and its licensors from and against any damages, costs, liabilities, s\nettlement amounts and/or expenses (including attorneys' fees) incurred in connection\nwith any claim, lawsuit or action by any third party that arises or results from the\nuse or distribution of any and all Programs and/or Software. \n\n\n\n3. Java Technology Restrictions. You may not modify the Java Platform Interface (\"JPI\n\", identified as classes contained within the \"java\" package or any subpackages of th\ne \"java\" package), by creating additional classes within the JPI or otherwise causing\n the addition to or modification of the classes in the JPI.  In the event that you\n create an additional class and associated API(s) which (i) extends the functionality\n of the Java platform, and (ii) is exposed to third party software developers for the\n purpose of developing additional software which invokes such additional API, you mus\nt promptly publish broadly an accurate specification for such API for free use by all\n developers.  You may not create, or authorize your licensees to create additional\n classes, interfaces, or subpackages that are in any way identified as \"java\", \"javax\n\", \"sun\" or similar convention as specified by Sun in any naming convention designati\non.\n\n\n\n4. Trademarks and Logos. You acknowledge and agree as between you and Sun that Sun ow\nns the SUN, SOLARIS, JAVA, JINI, FORTE, and iPLANET trademarks and all SUN, SOLARIS,\nJAVA, JINI, FORTE, and iPLANET-related trademarks, service marks, logos and other bra\nnd designations (\"Sun Marks\"), and you agree to comply with the Sun Trademark and Log\no Usage Requirements currently located at http://www.sun.com/policies/trademarks. Any\n use you make of the Sun Marks inures to Sun's benefit.\n\n\n\n5. Source Code. Software may contain source code that is provided solely for referenc\ne purposes pursuant to the terms of this Agreement.  Source code may not be redist\nributed unless expressly provided for in this Agreement.\n\n\n\n6. Termination for Infringement.  Either party may terminate this Agreement immedi\nately should any Software become, or in either party's opinion be likely to become, t\nhe subject of a claim of infringement of any intellectual property right.\n\n\n\nFor inquiries please contact: Sun Microsystems, Inc. 4150 Network Circle, Santa Clara\n, California 95054. \n\n(LFI#143217/Form ID#011801)\n";

	public CommPortIdentifier(String name, CommPort port, int type, CommDriver driver)
	{
		this.name = name;
		this.port = port;
		this.portType = type;
		commDriver = driver;
		this.owner = null;
		this.owned = false;
	}

	private static String findConfFile(String filename)
	{
		if (new File(filename).exists())
		{
			return filename;
		}

		StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"), File.pathSeparator);

		while (st.hasMoreTokens())
		{
			File p = null;
			File f = new File(st.nextToken());
			if (f.exists())
			{
				if (f.isDirectory())
					p = new File(f.getPath() + File.separator + filename);
				else if (f.getParent() != null)
					p = new File(f.getParent() + File.separator + filename);
				if ((p != null) && (p.exists()))
				{
					return p.getPath();
				}

			}

		}

		String defaultPath = System.getProperty("java.home") + File.separator + "lib" + File.separator + filename;

		if (new File(defaultPath).exists())
		{
			return defaultPath;
		}

		defaultPath = System.getProperty("java.home") + File.separator + "jre" + File.separator + "lib" + File.separator + filename;

		if (new File(defaultPath).exists())
		{
			return defaultPath;
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static CommDriver loadDriver(String className) throws IOException
	{
		CommDriver driverObj;
		if (className == null)
		{
			className = "com.sun.comm.SolarisDriver";
		}
		if (className != null)
		{
			try
			{
				driverObj = (CommDriver) Class.forName(className).newInstance();
			}
			catch (Exception e)
			{
				throw new IOException("Error instantiating class " + className + "\n" + e.getMessage());
			}

			return driverObj;
		}
		throw new IOException("javax.comm: platform driver class name = null\n                     (Check 'driver' property in javax.comm.properties)\n");
	}

	@SuppressWarnings("rawtypes")
	public static Enumeration getPortIdentifiers()
	{
		return Portmapping.getCommPortIdentifiers();
	}

	@SuppressWarnings("rawtypes")
	public static CommPortIdentifier getPortIdentifier(String portName) throws NoSuchPortException
	{
		synchronized (lock)
		{
			Enumeration e = Portmapping.getCommPortIdentifiers();
			while (e.hasMoreElements())
			{
				CommPortIdentifier cpi = (CommPortIdentifier) e.nextElement();
				if (cpi.name.equals(portName))
					return cpi;
			}
		}
		throw new NoSuchPortException();
	}

	@SuppressWarnings("rawtypes")
	public static CommPortIdentifier getPortIdentifier(CommPort port) throws NoSuchPortException
	{
		synchronized (lock)
		{
			Enumeration e = Portmapping.getCommPortIdentifiers();
			while (e.hasMoreElements())
			{
				CommPortIdentifier cpi = (CommPortIdentifier) e.nextElement();
				if (cpi.port == port)
					return cpi;
			}
		}
		throw new NoSuchPortException();
	}

	public static void addPortName(String portName, int portType, CommDriver driver)
	{
		SecurityManager sec = System.getSecurityManager();
		if (sec != null)
		{
			sec.checkDelete(propFilename);
		}
		Portmapping.add(new CommPortIdentifier(portName, null, portType, driver));
	}

	public synchronized CommPort open(String appname, int timeout) throws PortInUseException
	{
		try
		{
			if (this.owned)
			{
				this.waitingForPortAvailablity = true;
				fireOwnershipEvent(3);
				wait(timeout);
				this.waitingForPortAvailablity = false;
				if (this.owned)
					throw new PortInUseException(getCurrentOwner());
			}
		}
		catch (InterruptedException ie)
		{
			this.waitingForPortAvailablity = false;
			return null;
		}

		if ((this.port = commDriver.getCommPort(this.name, this.portType)) == null)
		{
			throw new PortInUseException("another application");
		}
		fireOwnershipEvent(1);

		this.owned = true;
		this.owner = appname;

		return this.port;
	}

	public CommPort open(FileDescriptor fd) throws UnsupportedCommOperationException
	{
		throw new UnsupportedCommOperationException();
	}

	public String getName()
	{
		return this.name;
	}

	public int getPortType()
	{
		return this.portType;
	}

	public String getCurrentOwner()
	{
		return this.owned ? this.owner : "Port currently unowned";
	}

	public boolean isCurrentlyOwned()
	{
		return this.owned;
	}

	synchronized void internalClosePort()
	{
		this.owned = false;
		this.owner = null;
		this.port = null;

		notifyAll();

		if (!this.waitingForPortAvailablity)
			fireOwnershipEvent(2);
	}

	public void addPortOwnershipListener(CommPortOwnershipListener listener)
	{
		if (this.listeners == null)
		{
			this.listeners = new Vector<CommPortOwnershipListener>();
		}
		if (!this.listeners.contains(listener))
			this.listeners.addElement(listener);
	}

	public void removePortOwnershipListener(CommPortOwnershipListener listener)
	{
		if (this.listeners != null)
			this.listeners.removeElement(listener);
	}

	void fireOwnershipEvent(int type)
	{
		Enumeration<CommPortOwnershipListener> e;
		if (this.listeners != null)
			for (e = this.listeners.elements(); e.hasMoreElements();)
				((CommPortOwnershipListener) e.nextElement()).ownershipChange(type);
	}

	static
	{
		String driverName = null;
		try
		{
			if (((driverName = CommProperties.getProperty("driver")) == null) && ((driverName = CommProperties.getProperty("DRIVER")) == null))
				driverName = CommProperties.getProperty("Driver");
			commDriver = loadDriver(driverName);
		}
		catch (Throwable t)
		{
			System.err.println("");
			t.printStackTrace();
		}

		if (commDriver != null)
		{
			commDriver.initialize();
			portmapFilename = findConfFile("portmap.conf");
		}

		propFilename = CommProperties.getPropFilename();

		if (System.getProperty("props") != null)
		{
			System.out.println("implementation version = 3.0");
			System.out.println("javax.comm.properties  = " + propFilename);
			System.out.println("portmap.conf           = " + portmapFilename);
			System.out.println("Driver class name      = " + driverName);
		}

		SecurityManager sec = System.getSecurityManager();
		if (sec != null)
		{
			sec.checkDelete(propFilename);
		}

		try
		{
			try
			{
				Portmapping.refreshPortDatabase(portmapFilename, commDriver, new PlatformPortBundle());
			}
			catch (Exception e)
			{
				e.getMessage();
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.getMessage();
			e.printStackTrace();
		}
	}
}
