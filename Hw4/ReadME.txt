***********************************************************************
Locale:
- Download the postgreSQL driver in the folder

- In a terminal window type
  javac DatabaseLocale.javac
  java -cp .:postgresql-42.2.2.jar DatabaseLocale

***********************************************************************
SSHtunneling:
- Download the postgreSQL driver in the folder.

- In a terminal window type
ssh -L 5433:dbstud.dei.unipd.it:5432 <username-dei>@login.dei.unipd.it

- In another terminal window type
javac DatabaseSSHtunneling.java
java -cp .:postgresql-42.2.2.jar DatabaseSSHtunneling
