from __future__ import print_function

from jinja2 import Environment, PackageLoader
import sqlite3
import re

env = Environment(loader=PackageLoader("scala-jdbi-sqlite-gen", "templates"))
con = sqlite3.connect("/home/andi/sqlite-latest.sqlite")


splitter = re.compile("\"(.*)\" ([^\s]*) .*")
def sql_to_typemap(sql):
	# split on newlines
	lines = str(sql).split("\n")
	# strip whitespace on the ends
	lines = map(lambda x:x.strip(), lines)
	# filter on matching the regex
	lines = filter(lambda x:splitter.match(x), lines)
	# run regex
	lines = map(lambda x:splitter.match(x).groups(), lines)
	# convert types
	lines = map(lambda x:(x[0], sqlite_type_to_scala_type(x[1])), lines)
	return lines

def sqlite_type_to_scala_type(t):
	t = t.split("(")[0]
	t = t.lower()
	lookup = {
			"integer": "Long",
			"float": "Float",
			"varchar": "String",
			"double": "Double",
			"char": "String",
			"decimal": "Double",
			"longtext": "String"
			}
	return lookup[t]

if __name__ == "__main__":
	cursor = con.cursor()
	cursor.execute("select * from sqlite_master where type='table';")
	r = cursor.fetchall()
	database = []
	for table in r:
		result = {}
		result["name"] = table[1]
		result["sql"] = table[4]
		result["entries"] = sql_to_typemap(result["sql"])
		database.append(result)
	#print(env.get_template("Types.scala").render(tables=database))
	print(env.get_template("Mappers.scala").render(tables=database))
	#print(env.get_template("Daos.scala").render(tables=database))
