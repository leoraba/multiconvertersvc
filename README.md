# README #

SpringBoot 2.2.4.RELEASE

## Functionalities: ##

1.  Convert XML to JSON
2.  Convert XML to CSV
3.  Convert JSON to XML
4.  Convert JSON to CSV



## URL endpoint ##
```
POST http://localhost:8081/converter
```

## 1. Convert XML to JSON ##

### Request ###

#### Headers: ####

* Content-Type: application/xml
* Accept: application/json

#### Body: ####
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

### Response ###
```
{
    "root": [
        {
            "var2": "My node 1 var2",
            "var1": "My node 1 var1"
        },
        {
            "var2": "My node 2 var2",
            "var1": "My node 2 var1"
        }
    ]
}
```


## 2. Convert XML to CSV ##

### Request ###

#### Headers: ####

* Content-Type: application/xml
* Accept: text/plain

#### Body: ####
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

### Response ###
```
var1,var2
My node 1 var1,My node 1 var2
My node 2 var1,My node 2 var2
```

## 3. Convert JSON to XML ##

### Request ###

#### Headers: ####

* Content-Type: application/json
* Accept: application/xml

#### Body: ####
```
{
    "document": [
        {
            "description": "this is my description 1",
            "title": "my Title 1"
        },
        {
            "description": "this is my description 2",
            "title": "my Title 2"
        },
        {
            "description": "this is my description 3",
            "title": "my Title 3"
        },
        {
            "description": "this is my description 4",
            "title": "my Title 4"
        }
    ]
}
```

### Response ###
```
<document>
    <description>this is my description 1</description>
    <title>my Title 1</title>
</document>
<document>
    <description>this is my description 2</description>
    <title>my Title 2</title>
</document>
<document>
    <description>this is my description 3</description>
    <title>my Title 3</title>
</document>
<document>
    <description>this is my description 4</description>
    <title>my Title 4</title>
</document>
```


## 4. Convert JSON to CSV ##

### Request ###

#### Headers: ####

* Content-Type: application/json
* Accept: text/plain

#### Body: ####
```
{
    "document": [
        {
            "description": "this is my description 1",
            "title": "my Title 1"
        },
        {
            "description": "this is my description 2",
            "title": "my Title 2"
        },
        {
            "description": "this is my description 3",
            "title": "my Title 3"
        },
        {
            "description": "this is my description 4",
            "title": "my Title 4"
        }
    ]
}
```

### Response ###
```
description,title
this is my description 1,my Title 1
this is my description 2,my Title 2
this is my description 3,my Title 3
this is my description 4,my Title 4
```

