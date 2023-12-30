keytool -genkey -v -keystore key1.jks -alias key1 -keyalg RSA -keysize 4096 -validity 10000
Enter keystore password: P@$$w0rd
What is your first and last name: koustubh.desai
What is the name of your organizational unit: none
What is the name of your organization: none
What is the name of your City or Locality: pune
What is the name of your State or Province: mah
What is the two-letter country code for this unit: in

--- Did followiing using goolge play console pem key -----
Export the upload key certificate as a PEM file using the following command. Replace the arguments in <>.
keytool -export -rfc -keystore <upload-keystore.jks> -alias <upload> -file <upload_certificate.pem>

keytool -export -rfc -keystore my-release-key.keystore -alias my-key-alias -file upload_certificate.pem

------- Stack overflow ------
On https://stackoverflow.com/questions/52507156/your-android-app-bundle-is-signed-with-the-wrong-key-ensure-that-your-app-bundl see the response by Blundering Philosopher

---

keytool -export -rfc -keystore key1.jks -alias key1 -file upload_certificate.pem
