String homeScreenInputText = "";
//REPLACE ^variable with inputtedUser / inputtedPassword / newUser / newPassword

void displayBox(int x1, int y1, int w, int l, String text, int textSize) {
  fill(0);
  rect(x1, y1, w, l, 100);

  fill(255);
  textSize(textSize);
  textAlign(CENTER, CENTER);
  text(text, x1, y1, w, l);
}

void keyPressed() {
  if (keyCode != SHIFT && keyCode != ENTER && keyCode != CONTROL) homeScreenInputText += key;
}
