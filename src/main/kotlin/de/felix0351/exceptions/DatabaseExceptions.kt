package de.felix0351.exceptions



class WrongSQLTypeException(type: String) : IllegalArgumentException("SQL Driver with the name $type can't be found!")
class MissingArgumentsException() : IllegalArgumentException("There are missing arguments for the sql connection!")
