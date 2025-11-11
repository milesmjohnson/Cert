# 1) make a sample JAR (optional—only if you want to sign a JAR)
mkdir signdemo && cd signdemo
cat > Hello.java << 'EOF'
public class Hello { public static void main(String[] args){ System.out.println("hi"); } }
EOF
javac Hello.java && jar --create --file Hello.jar Hello.class

# 2) create a self-signed cert & keystore
keytool -genkeypair \
 -alias mykey \
 -keyalg RSA -keysize 2048 -validity 3650 \
 -keystore keystore.jks \
 -dname "CN= Miles johnson, OU=CS Dept, O= NC A&T, L=Greensboro, ST=NC, C=US, emailAddress= mjohnson9@aggies.ncat.edu" \
 -storepass StorePass123! -keypass KeyPass123!

# 3) export a PKCS#12 (.p12/.pfx) you can use in Word/Acrobat
keytool -importkeystore \
 -srckeystore keystore.jks -srcalias mykey -srcstorepass StorePass123! \
 -destkeystore mycert.p12 -deststoretype PKCS12 -deststorepass P12pass123!

# 4) (optional) sign the JAR
jarsigner -keystore keystore.jks -storepass StorePass123! -keypass KeyPass123! Hello.jar mykey

# 5) (optional) verify
jarsigner -verify -verbose -certs Hello.jar
