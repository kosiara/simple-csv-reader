Simple CSV reader

#### Features
- efficiently reads csv columns to String[]
- can map csv to ```List<MappedObject>```


gradle build system, JUnit tests in src/test/com.bkosarzycki.util.csv


#### Usage

```java
 CSVReaderWriter csvRW = new CSVReaderWriter();
 csvRW.open("fileName.csv", CSVReaderWriter.Mode.Read);
 String[] result = csvRW.read();
 csvRW.close();
```
#### Object mapping usage:
```java
CSVObjectMapper<SampleCSVRow> mapper = new CSVObjectMapper("fileName.csv", SampleCSVRow.class);
SampleCSVRow row = mapper.read(SampleCSVRow.class);
mapper.close();
```

