package de.htwg.sa.database

import slick.jdbc.H2Profile.api._

class Saves(tag: Tag) extends Table[(Int, String, String)](tag, "SAVES") {

  def id = column[Int]("SAVE_ID", O.PrimaryKey)

  def userName = column[String]("USER_NAME")

  def save = column[String]("SAVE_JSON")

  def * = (id, userName, save)

}
