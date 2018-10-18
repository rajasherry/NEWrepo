import java.util.zip.*

import groovy.io.FileType

def list = []

def dir = new File("/Users/sheharyaryousaf/Desktop/Files")
dir.eachFileRecurse (FileType.FILES) { file ->
	if(file.toString().contains(".gz"))
		list << file
}

list.each {
	println it.toString()

	def fileToBeMasked = it.toString().replace(".gz", "")
	println fileToBeMasked

	def gzis =new GZIPInputStream(new FileInputStream(it));

	byte[] buffer = new byte[1024];

	def out = new FileOutputStream(fileToBeMasked);

	int len;
	while ((len = gzis.read(buffer)) > 0) {
		out.write(buffer, 0, len);
	}

	gzis.close();
	out.close();

	if(it.delete()) {
		println "deleted successfully"
	} else {
		println "Error in File delete"
	}

	System.out.println("Done");

}



