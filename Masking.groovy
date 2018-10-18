
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import groovy.io.FileType

def list = []

def dir = new File("/Users/sheharyaryousaf/Desktop/Files")
dir.eachFileRecurse (FileType.FILES) { file ->
	if(file.toString().contains(".json"))
		list << file
}

list.each {
	println it.toString()

	File fileToBeMasked = new File(it.toString())
	String maskedFile = it.toString().replace(".json", "_masked.json")
	def fileWriter = new FileWriter(maskedFile)

	def lineNo = 1
	def line
	def jsonSlurper = new JsonSlurper()


	fileToBeMasked.withReader { reader ->
		while ((line = reader.readLine())!=null) {

			String rec = "${line}"

			def jsonObject = jsonSlurper.parseText(rec)

			def content = "${line}"
			def slurped = new JsonSlurper().parseText(content)
			def builder = new JsonBuilder(slurped)
			builder.content.fields.db_elapsed_time = '9999.9999'


			if (jsonObject.fields.input_parameters.acct != null &&
				jsonObject.fields.input_parameters.acct.billing_group != [null]) {


				builder.content.fields.input_parameters.acct.billing_group[0][0].first_name = 'XXXXXXX'
				builder.content.fields.input_parameters.acct.billing_group[0][0].last_name = 'XXXXXXXX'
				builder.content.fields.input_parameters.acct.billing_group[0][0].address1 = 'XXXXXXXXX'
				builder.content.fields.input_parameters.acct.billing_group[0][0].phone = 'XXX-XXX-XXXX'
			}
			if (jsonObject.fields.input_parameters.acct != null &&
				jsonObject.fields.input_parameters.acct != [null] &&
				jsonObject.fields.input_parameters.acct.empty == false) {

				builder.content.fields.input_parameters.acct[0].first_name = 'XXXXXXXXX'
				builder.content.fields.input_parameters.acct[0].last_name = 'XXXXXXXXX'
				builder.content.fields.input_parameters.acct[0].address1 = 'XXXXXXXX'
				builder.content.fields.input_parameters.acct[0].phone = 'XXX-XXX-XXXX'
				builder.content.fields.input_parameters.acct[0].email = 'XXXXXXXXXXXXXXXX'
			}

			String maskedRec = builder.toString()

			builder.writeTo(fileWriter)

			fileWriter.flush() /* to push the data from  buffer to file */
			fileWriter.flush()

			def fileContents = new File(maskedFile).text

			lineNo++
		}
		reader.close()
	}

	if(fileToBeMasked.delete()) {
		println "deleted successfully"
	} else {
		println "Error in File delete"
	}
}
