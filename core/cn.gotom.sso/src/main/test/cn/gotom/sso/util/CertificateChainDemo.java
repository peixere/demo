package cn.gotom.sso.util;


//import org.bouncycastle.jce.X509Principal;
//import org.bouncycastle.x509.X509V3CertificateGenerator;

public class CertificateChainDemo
{

	public String caName = "caroot";
	public String caPasswd = "******";

	public String keyStorePasswd = "******";

	public String keyStorePath = "/root/.keystore";

	public String userDN = "CN=loong,   OU=CS,   O=HUST ,L=Wuhan, ST=Hubei, C=CN";
	public String userAlias = "loong"; // 用户别名

	public CertificateChainDemo()
	{
	}
/*
	public boolean generateX509Certificate(String userCertPath)
	{
		try
		{
			FileInputStream in = new FileInputStream(keyStorePath);
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(in, keyStorePasswd.toCharArray());
			in.close();

			// Get CA private key.
			PrivateKey caPrivateKey = (PrivateKey) ks.getKey(caName, caPasswd.toCharArray());
			System.out.println("\nCA private key:\n" + caPrivateKey);

			// Get CA DN.
			Certificate c = ks.getCertificate(caName);
			X509Certificate t = (X509Certificate) c;
			String caDN = t.getIssuerDN().toString();
			// CN:姓氏、名字 OU:组织单位名称 O:组织名称 L:城市、区域 C:国家代码
			System.out.println("\nCA DN:\n" + caDN);
			
			X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
			KeyPair KPair = RSAKeyPairGenDemo.getRSAKeyPair(1024);
			System.out.println("\nuser private key:\n" + KPair.getPrivate());
			System.out.println("\nuser public key:\n" + KPair.getPublic());

			v3CertGen.setSerialNumber(BigInteger.valueOf(1));
			v3CertGen.setIssuerDN(new X509Principal(caDN));
			// 证书有效期起
			v3CertGen.setNotBefore(new Date(System.currentTimeMillis()));
			// 证书有效期止
			v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)));
			v3CertGen.setSubjectDN(new X509Principal(userDN));
			v3CertGen.setPublicKey(KPair.getPublic());
			v3CertGen.setSignatureAlgorithm("SHA1WithRSA");

			// Generate user certificate signed by ca private key.
			X509Certificate cert = v3CertGen.generateX509Certificate(caPrivateKey);
			FileOutputStream out = new FileOutputStream(userCertPath);
			out.write(cert.getEncoded());
			out.close();

			// Add user entry into keystore
			ks.setCertificateEntry(userAlias, cert);
			out = new FileOutputStream(keyStorePath);
			ks.store(out, caPasswd.toCharArray());
			out.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public void listX509CertificateInfo(String certFile)
	{
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new FileInputStream(certFile));
			System.out.println("\nIssuerDN:" + x509Cert.getIssuerDN());
			System.out.println("Signature   alg:" + x509Cert.getSigAlgName());
			System.out.println("Version:" + x509Cert.getVersion());
			System.out.println("Serial   Number:" + x509Cert.getSerialNumber());
			System.out.println("Subject   DN:" + x509Cert.getSubjectDN());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean Verify(String certPath)
	{
		Certificate cert;
		PublicKey caPublicKey;

		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			FileInputStream in = new FileInputStream(certPath);
			cert = cf.generateCertificate(in);
			in.close();
			X509Certificate t = (X509Certificate) cert;
			Date timeNow = new Date();
			t.checkValidity(timeNow);

			in = new FileInputStream(keyStorePath);
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(in, keyStorePasswd.toCharArray());
			in.close();
			caPublicKey = ks.getCertificate(caName).getPublicKey();
			System.out.println("\nCA public key:\n" + caPublicKey);
			try
			{
				cert.verify(caPublicKey);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				System.out.println("no pass.\n");
				e.printStackTrace();
			}
			System.out.println("\npass.\n");
		}
		catch (CertificateExpiredException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (CertificateNotYetValidException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (CertificateException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (KeyStoreException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (NoSuchAlgorithmException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}

	public static void main(String args[])
	{
		String userCertPath = "/home/loong/loong.crt";
		CertificateChainDemo ccd = new CertificateChainDemo();
		ccd.generateX509Certificate(userCertPath);
		ccd.listX509CertificateInfo(userCertPath);
		ccd.Verify(userCertPath);
	}*/
}