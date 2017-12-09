func main() : void
{
    a : i8 = 127;
    b : i8 = 2;
    { c : i8 = a / b * 2 / 4 + (88 % 5); dump c; }
    { c : i8 = 2 / 4 + a / b * (88 % 3); dump c; }
}
