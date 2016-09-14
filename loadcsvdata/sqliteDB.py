"""Creates a SQLite3 database (reviews.db) and the reviews table.

The table reflects the fields provided by the Google Play Developer
Console data dumps, which are formatted as Unicode CSV files.

This is a basic program. You're welcome to improve it. For me it
serves a basic project requirement and will do.
"""
from __future__ import print_function
import sqlite3

class Error(Exception):
    """Base exception class, as recommented by Google Style Guide.
    https://google.github.io/styleguide/pyguide.html#Exceptions
    """
    pass

def create_connection(db_file):
    """ create a database connection to the SQLite database
        specified by db_file
    :param db_file: database file
    :return: Connection object or None
    """
    try:
        conn = sqlite3.connect(db_file)
        return conn
    except Error as error:
        print(error)
    return None

def create_table(conn, create_table_sql):
    """ create a table from the create_table_sql statement
    :param conn: Connection object
    :param create_table_sql: a CREATE TABLE statement
    :return:
    """
    try:
        cursor = conn.cursor()
        cursor.execute(create_table_sql)
    except Error as error:
        print(error)

def main():
    """The workhorse that does everything."""
    database = 'reviews.db'
    create_reviews_table = '''CREATE TABLE reviews
            (package TEXT NOT NULL,
             appversioncode INTEGER,
             appversionname TEXT,
             reviewerlanguage TEXT,
             device TEXT,
             reviewsubmitted DATETIME NOT NULL,
             reviewsubmittedmillis INTEGER,
             reviewlastupdate DATETIME,
             reviewlastupdatemillis INTEGER,
             starrating INTEGER NOT NULL,
             reviewtitle TEXT,
             reviewtext TEXT,
             developerreply DATETIME,
             developerreplymillis INTEGER,
             developerreplytext TEXT,
             reviewlink TEXT);'''

    # create a database connection
    conn = create_connection(database)
    if conn is not None:
        create_table(conn, create_reviews_table)
    else:
        print("Error! cannot create the database connection.")

if __name__ == '__main__':
    main()
