# README #

SpringBoot 2.2.4.RELEASE

### URL endpoint ###
```
POST http://localhost:8081/converter
```

### Headers: Convert XML to JSON ###

* Content-Type: application/xml
* Accept: application/json


### Headers: Convert XML to CSV ###

* Content-Type: application/xml
* Accept: text/plain

### Request Body ###
```
<root>
	<node>
		<var1>My node 1 var1</var1>
		<var2>My node 1 var2</var2>
	</node>
	<node>
		<var1>My node 2 var1</var1>
		<var2>My node 2 var2</var2>
	</node>
</root>
```

