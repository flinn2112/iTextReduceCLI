
Reduces the size of PDF Files.
iTextReduceCLI will not delete the original files.

USAGE:

java -jar iTextReduceCLI.jar [OPTIONS]

OPTIONS:

-mode
f(single file)
d(directory)
b(batch)
-rename

SAMPLES:
========
1. Processing a single file .
    java -jar iTextReduceCLI.jar -mode f -in d:\big.pdf -out d:\big_min.pdf
   on Unix
    java -jar iTextReduceCLI.jar -mode f -in /var/www/big.pdf -out /var/www/big_min.pdf

2. Processing a complete directory(no recursive processing of subdirectories)
    java -jar iTextReduceCLI.jar -mode d -in d:\www -namePrefix min_
      will change filenames of processed files from 
      myPdf.pdf to min_myPdf.pdf
 or
    java -jar iTextReduceCLI.jar -mode d -in d:\www -namePostfix _min
      will change filenames of processed files from 
      myPdf.pdf to myPdf_min.pdf
3. Processing a fixed batch of files 
   A file containing path to files each in one line can be processed.
    java -jar iTextReduceCLI.jar -mode b [-enc ISO-????-?] -in PATH_TO_TEXTFILE -namePrefix min_
   or with postfix
    java -jar iTextReduceCLI.jar -mode b [-enc ISO-????-?]  -in PATH_TO_TEXTFILE -namePostfix _min

	Default encoding is UTF-8, if you use different encodings then specify using arg -enc.
        Invalid encodings(typos etc.) will lead to an exception.

I. In case of trouble:
   a: 
        java.lang.NoClassDefFoundError: 
              org/bouncycastle/asn1/ASN1Encodable
      Not solved, occurs on some files.
      Source lies deep in iText Lib. - sorry.
      Cannot process these currently.
      When using batches then you have to remove these files
      from your list.
   b: 
	 Batch, Filenames having special characters(ÄÜÖ etc.) fail to be processed. 
         Solution: use arg -enc to set file encoding to whatever suitable(i.e. -enc ISO-8859-1)
   
