parkplan
========

Dont forget to create /src/main/resources/app.properties file with the following properties:
<code>
client.id=
client.secret=
apptoken.admin=
apptoken.user=

redirectUri=

database.driver=com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource
database.url.bbdd=jdbc:mysql://myserver:3306/parkplan
database.username=
database.password=
</code>

Also edit the /src/main/resources/META-INF/context.xml to match your datasource specs
