Table table;

void NAMETHISACTION() {
  table = loadTable("score.csv", "header");
  println(table.getRowCount() + " total rows in table"); 
  println("hello");
  TableRow score = table.addRow();
  score.setString("score", Float.toString(random(0, 4)));
  println(table.getRowCount());
  saveTable(table, "data/score.csv");

  //Use below code to initialize the score sheet
  /*
  Table table;
   table = new Table();
   table.addColumn("User 1 Score");  
   saveTable(table, "data/score.csv");
   */
}
