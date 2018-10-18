import java.util.zip.*

import groovy.io.FileType

def list = []

def dir = new File("/Users/sheharyaryousaf/Desktop/Files")
dir.eachFileRecurse (FileType.FILES) { file ->
	if(file.toString().contains(".json"))
		list << file
}

list.each {

	println it.toString()

	String zipFileName = it.toString().replace("_masked", "").concat(".gz")
	String fileToBeZip = it.toString()

	//**************Zip files*****************

	def fis = new FileInputStream(fileToBeZip);
	def fos = new FileOutputStream(zipFileName);
	def gzipOS = new GZIPOutputStream(fos);
	byte[] buffer = new byte[1024];
	int len;
	while((len=fis.read(buffer)) != -1){
		gzipOS.write(buffer, 0, len);
	}
	//close resources

	gzipOS.close();
	fos.close();
	fis.close();

	File fileToBeDeleted = new File(fileToBeZip);

	if(fileToBeDeleted.delete()) {
		println "deleted successfully"
	} else {
		println "Error in File delete"
	}
}





