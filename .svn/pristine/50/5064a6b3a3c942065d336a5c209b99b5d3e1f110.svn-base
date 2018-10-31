package com.hnjk.core.foundation;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;

public class ABchatRSA {

	  /** Creates a new instance of ABchatRSA */
    /*构建需要的函数变量*/
    private KeyPairGenerator kePaGen=null;                //秘密钥匙生成器;
    private KeyPair          keyPair=null;                //钥匙对，公尺 和米尺；
    private PublicKey        publicKey=null;              //共匙；
    private PrivateKey       privateKey=null;             //密匙；
    private int             keySize    =512;               //密匙长
   
    public ABchatRSA(int keysize) {
        this.keySize= keysize;
        try{
        this.kePaGen= KeyPairGenerator.getInstance("RSA"); //
        this.kePaGen.initialize(this.keySize);  
        //
        this.keyPair=this.kePaGen.genKeyPair();
        this.privateKey=this.keyPair.getPrivate();
        this.publicKey=this.keyPair.getPublic();
         //this.abcharRsaCipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }catch( Exception err){
            err.printStackTrace();
        }
      
    }
    public PublicKey getPublicKey()
    {
        return this.publicKey;
    }
    public PrivateKey getPrivateKey()
    {
        return this.privateKey;
    }
    public static String encripyRSA(String platxt,PublicKey publickey)
    {
        String cipherStr=null;                              //返回的加密后的字符串;     
        byte[]plainByte=null;                              //获得明文的byte数组;
        byte[]cipherByte;                                    //产生秘闻的byte数组;                         
        Cipher cipher =null;
        try{         
        plainByte=platxt.getBytes("ISO-8859-1");
        cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publickey);
        cipherByte=cipher.doFinal(plainByte);       
        cipherStr=new String(cipherByte,"ISO-8859-1");
         }catch(Exception err){
            err.printStackTrace();
            System.out.println("error in en: "+err.toString());
        }
        return cipherStr;
    }
   
    public static String decripyRSA(String cphtxt,PrivateKey privateKey)
    {
         byte[] cipherByte =null;                             //获得秘闻的byte数组;       
         byte[] plainByte   =null;                             //解密后的明文数组;
         String   plainStr    =null;                            //解密后的明文数组;
         Cipher   cipher      =null;                            //加密用;
        try{
            cipherByte       =cphtxt.getBytes("ISO-8859-1");    //统一使用该种编码方式;
            cipher =Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            plainByte=cipher.doFinal(cipherByte);
            plainStr=new String(plainByte,"ISO-8859-1");        
        }catch(Exception err)
        {

            err.printStackTrace();
        }
        return plainStr;
    }
   
   
    public static void main(String []args) throws Exception
    {
        ABchatRSA arsa=new ABchatRSA(512);
        String en= BaseSecurityCodeUtils.base64Encode(ABchatRSA.encripyRSA("HELLO world123131231231232121212121212121212121",arsa.getPublicKey()).getBytes());     
        
        System.out.println(en);
     
        String de=ABchatRSA.decripyRSA(new String (BaseSecurityCodeUtils.base64Decode(en)),arsa.getPrivateKey());
        System.out.println(de);       
    }
}
