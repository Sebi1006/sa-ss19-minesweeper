package hwtg

import slick.jdbc.H2Profile.api._

// A Suppliers table with 6 columns: id, name, street, city, state, zip
class Saves(tag: Tag) extends Table[(Int, String, String)](tag, "SAVES") {
  def id = column[Int]("SAVE_ID", O.PrimaryKey)
  def userName = column[String]("USER_NAME")
  def save = column[String]("SAVE_JSON")
  
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, userName, save)

}

