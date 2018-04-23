/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itextreduce;
//import com.hcc_medical.mavenproject1.* ;
import com.itextpdf.licensekey.LicenseKey;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Hashtable ;

/**
 *
 * @author frank
 */
public class ITextReduce {
    
    
    
    public static Hashtable htOptions = new Hashtable();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String strIn  = null ;
        String strOut = null ;
        String usage = "USAGE: \n java -jar ITextReduce"
                 + "\t-mode {f|d|b} [-enc UTF-8] \n\t-in {FULLPATH_TO_PDF|DIRECTORY} "
                 + "\n\t[-out FULLPATH_TO_REDUCED_PDF|DIRECTORY]";
         
        if( args.length < 2 ){
          System.out.println(usage);
          return ;
        } 
         
        for(int i=0;i<args.length;i++) {
            if(i %2 == 0){
                ITextReduce.htOptions.put(args[i], args[i+1]) ;
            }                
        }
        
        try {
            ITextReduce.processOptions(ITextReduce.htOptions) ;
        } catch (Exception ex) {
            Logger.getLogger(ITextReduce.class.getName()).log(Level.SEVERE, null, ex);
        }
    
   
      if( null == strOut || strOut.isEmpty()){
          strOut = strIn +  ".min.pdf" ; 
      }
      
        /*
        String strSrc = System.getProperty("user.dir") 
                 + File.separator + "in.pdf" ;
        String strDst = System.getProperty("user.dir") 
                 + File.separator + "out.pdf" ;
        */
        //Load License for iText
        String strLFile = System.getProperty("user.dir") 
                 + File.separator + "itextkey1519992068869_0.xml" ;
         //System.out.println("reducePDF");
         //System.out.println(strLFile) ;
         //LicenseKey.loadLicenseFile(strLFile) ;
         //System.out.println(LicenseKey.getLicenseeInfo()) ;
        //int i =  com.hcc_medical.mavenproject1.mainStuff.test(f);

      //      com.hcc_medical.mavenproject1.mainStuff.reducePdf(strIn, strOut);
//       ITextReduce.processBatch("X:\\SharedDownloads\\Projekte\\hcc\\hcc_search\\dist\\logs\\reviewResultPart.txt") ; 
        System.out.println("END!");
    }
    
    private static int processOptions(Hashtable ht) throws Exception{
        String strOption = null ;
        int iRet = 0 ;
        strOption = (String)ht.get("-mode") ;
        if( null == strOption){
            throw new Exception("!No arguments found.") ;
        }
        
        switch(strOption){
            case "f": //file
                
            case "d": //dir
            case "b": {
            try {
                //batch
                ITextReduce.processBatch(ht) ;
            } catch (Exception ex) {
                Logger.getLogger(ITextReduce.class.getName()).log(Level.SEVERE, null, ex);
                throw ex ;
            }
        }
            default:
               iRet = 0 ; 
        }
        
        return iRet ;
    }

    private static String makeFilename(String strPath, String strIn, String strFileExt, String strPrefix, String strPostfix){
        StringBuilder sb = new StringBuilder();
        if( null == strPrefix && null == strPostfix ){
            return null ;
        }
        if( null != strPrefix && !strPrefix.isEmpty() ){
            sb.append(strPrefix) ;
            sb.append(strPath) ;
            sb.append(File.separator) ;
            sb.append(strIn) ;
            
        }else if( null != strPostfix && !strPostfix.isEmpty() ){
            sb.append(strPath) ;
            sb.append(File.separator) ;
            sb.append(strIn) ;
            sb.append(strPostfix) ;
        }
        sb.append(".") ;
        sb.append(strFileExt) ;
        return sb.toString() ;
    }
    
    private static void processLine(fileStuff fsBatchFile, String strLine, String strTargetDir) throws java.lang.Exception{
        String[] rParts = null ;
        String strOut = null ;
        fileStuff fs = null ; //file object for this line
        rParts = strLine.split(";") ;
        System.out.println(strLine);
        if( null != rParts[0] && !rParts[0].isEmpty()){            
            fs = new fileStuff(rParts[0], (String)fsBatchFile.m_htOptions.get("-namePrefix"), 
                    (String)fsBatchFile.m_htOptions.get("-namePostfix")) ;
            if( null != (String)fsBatchFile.m_htOptions.get("-out")){
                fs.setTargetDir ((String)fsBatchFile.m_htOptions.get("-out")) ;
            }
            strOut = ITextReduce.makeFilename(fs.getTargetDir(), fs.m_nakedName, fs.m_ext, fs.m_prefix, fs.m_postfix) ;
            System.out.println("Processing [" +  fs.m_fullPath + "->" + strOut + "]") ;
            try{
                com.hcc_medical.mavenproject1.mainStuff.reducePdf(fs.m_fullPath, strOut);
            }catch(Exception ex){
                System.err.println("processLine - error: " + ex.toString());
            }
        }
        else{
            throw Exception("No file name found on this line") ;
        }
        //com.hcc_medical.mavenproject1.mainStuff.reducePdf(strIn, strOut);
    }
    
    /*
        
    */
    private static void processBatch(Hashtable ht) throws Exception{
        String strValue = null ;
        File f = null ;
        fileStuff fs = null ;
        strValue = (String)ht.get("-in") ;
        if(null == strValue){
            throw new Exception("No value for argument '-in' found.") ;
        }
        //OK, but, file needs to be checked of course
        f = new File(strValue) ;
        if( null == f || false == f.exists() || false == f.canRead()){
            throw new Exception("Batch file [" + strValue + "] is not accessible") ;
        }
        fs = new fileStuff(f) ;  //create an object for the batch file
        fs.m_htOptions = ht ;
        processBatch(fs) ;        
    }
    
    private static void processBatch( fileStuff fsBatch) throws java.lang.Exception{
    FileInputStream fis = null ;
      BufferedReader br = null ;
      StringBuilder sb = new StringBuilder() ;
      String strTmp = new String() ;
      String strEnc = (String)fsBatch.m_htOptions.get("-enc")  ;
      
      if( null == strEnc || strEnc.isEmpty()){
          strEnc =  "UTF-8" ;
      }
      
      try {
          fis = new FileInputStream(new File(fsBatch.m_fullPath));  //open batch file
          br = new BufferedReader(new InputStreamReader(fis, strEnc)) ; //"UTF-8"
          System.out.println("processCfgFile - opened: " + fsBatch.m_fullPath );
        } catch (Exception ex) {
          // at least on windows, some temporary files raise this exception with an "access denied" message
          // checking if the file can be read doesn't help    
            System.err.println("processCfgFile - error: " + ex.toString());
            throw ex ;
        }

        try{             
             while ((strTmp = br.readLine()) != null) {
                  if(strTmp.startsWith("#")){ //a comment line                       
                    }else{
                      try{
                        ITextReduce.processLine(fsBatch, strTmp, null); //use filename
                      }catch( Exception ex){
                          System.err.println( ex.toString());
                      }
                  }                   
             }
             br.close();
             fis.close();
        } catch (java.io.IOException ex){
           System.err.println(ex.toString());
           throw ex ;
        }finally{
           
           
        }     
     }

    private static Exception Exception(String no_file_name_found_on_this_line) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

   class fileStuff{
        public  File      m_file = null ;
        public  String    m_ext = null ;
        public  String    m_name = null ;
        public  String    m_nakedName = null ; //name without ext
        public  String    m_fullPath = null ;
        public  String    m_prefix = null ;
        public  String    m_postfix = null ;
        private String    m_targetDir = null ;
        public  Hashtable m_htOptions = null ;
        
        
        public fileStuff(File f) throws Exception{
            init(f) ;
        }
        public fileStuff(String strPath, String prefix, String postfix) throws Exception{
            File f = new File(strPath) ;
            if( null == f ){
                throw new Exception("Something's wrong with file [" + strPath + "]") ;
            }
            
           if( !f.canRead() ){
               throw new Exception("Cannot read file [" + strPath + "]") ;
           }
            
            m_prefix  = prefix ;
            m_postfix = postfix ;
            init(f) ;
        }
        
        public fileStuff(File f, String prefix, String postfix) throws Exception{
            if( null == f || !f.canRead()){
                throw new Exception("Something's wrong with that file - check it" ) ;
            }
            m_prefix  = prefix ;
            m_postfix = postfix ;
            init(f) ;
        }
        
        private void init(File f) throws Exception{
            String strPath = null ;
            m_ext = getExtension(f.getName()) ;
            m_name = f.getName() ;
            m_nakedName = m_name.replace( "." + m_ext, "") ;
            m_fullPath = f.getAbsolutePath() ;
            m_file = f ;
            strPath = f.getAbsolutePath() ;
            strPath = strPath.replace(f.getName(), "") ;
            setTargetDir(strPath) ;
        }
        
        //!set targetDir has to check for some
        public void setTargetDir(File f) throws Exception{
            if(!f.isDirectory()){
                throw new Exception(f.getPath() + " is not a directory") ;
            }
            m_targetDir = f.getPath() ; //todo: check path
            if(m_targetDir.endsWith(File.separator)){
                m_targetDir = m_targetDir.substring(0, m_targetDir.length()-1) ;
            } 
        }
        //!set targetDir has to check for some
        public void setTargetDir(String strPath) throws Exception{
            m_targetDir = strPath ; //todo: check path
            if(m_targetDir.endsWith(File.separator)){
                m_targetDir = m_targetDir.substring(0, m_targetDir.length()-1) ;
            } 
        }
        
        public String getTargetDir(){
            return m_targetDir ;
        }
        
        public  String getExtension(String strFilename){
     
            String strRet = null ;
            int iPos = 0 ;
            if(null == strFilename) return "" ;

            /* it is quite rudimentary here: get the string after the last '.'.
             * But the string may start with '.' - then it is not an extension
            */
            if( strFilename.startsWith(".") ){
               return "" ;
            }
            iPos = strFilename.lastIndexOf(".");
            if(iPos > 0){
                strRet = strFilename.substring( iPos + 1 ) ;
            }

            return strRet ;
         }
        
    }
