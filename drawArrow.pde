void drawLeftArrow(int x1, int y1, int l) {
  stroke(255);
  strokeWeight(10);
  line(x1, y1, x1+l, y1);
  line(x1+l, y1, x1+l/2, y1+l/4);
  line(x1+l, y1, x1+l/2, y1-l/4);
}

void drawRightArrow(int x1, int y1, int l) {
  stroke(255);
  strokeWeight(10);
  line(x1, y1, x1, y1+l);
  line(x1, y1+l, x1+l/4, y1+l/2);
  line(x1, y1+l, x1-l/4, y1+l/2);
}
