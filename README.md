# CSV Summary Tool

A command-line tool that reads any CSV file and prints per-column statistics — count, null rate, and (for numeric columns) min, max, and mean. Built to explore how much you can infer about a dataset's quality and shape before doing any real analysis on it.

## Features

- Detects numeric vs. non-numeric columns automatically
- Reports missing/blank values per column, as a percentage
- Computes min, max, and mean for numeric columns
- Handles malformed or short rows without crashing
- Supports custom delimiters (comma, semicolon, etc.)

## Example

Given a file `sample.csv`:

```csv
name,age,score
Alice,30,85.5
Bob,25,90
Charlie,,78
Dana,40,
Eve,abc,88
```

Running:

```bash
java -cp target/classes com.nolanhp.CsvSummarizer sample.csv
```

Produces:

```
Column: name
count=5
null count=0
null percentage = 0.00
(non-numeric)

Column: age
count=5
null count=1
null percentage = 20.00
(non-numeric)

Column: score
count=5
null count=1
null percentage = 20.00
min = 78.00
max = 90.00
mean = 85.38
```

Note: the `age` column is marked non-numeric because one value ("abc") failed to parse — a single bad value flips the whole column.

## Usage

```bash
java -cp target/classes com.nolanhp.CsvSummarizer <file.csv> [delimiter]
```

- `<file.csv>` — path to the CSV file (required)
- `[delimiter]` — a single character to split on (optional, defaults to `,`)

Example with a semicolon-delimited file:

```bash
java -cp target/classes com.nolanhp.CsvSummarizer data.csv ";"
```

## Building and testing

```bash
mvn compile   # build
mvn test      # run the test suite
mvn package   # produce a jar
```

## Tech

Java 17, Maven, JUnit 5.

## Design notes

- `ColumnStats` tracks running totals (count, sum, min/max, null count) per column and computes derived stats (mean, null rate) on demand.
- `CsvSummarizer.summarize()` parses a file into a `Map<String, ColumnStats>`; `printReport()` handles all display logic separately, so the parsing logic can be tested independently of console output.
- Short/malformed rows are handled gracefully — missing fields are treated as blank rather than throwing an exception.
