func main() : void
{
    a : i32;
    b : i32;

    a = 1;
    b = 2;

    dump a == a;
    dump b == b;
    dump b == a;
    dump a == b;
    dump a != a;
    dump b != b;
    dump b != a;
    dump a != b;
    dump a <  a;
    dump b <  b;
    dump b <  a;
    dump a <  b;
    dump a <= a;
    dump b <= b;
    dump b <= a;
    dump a <= b;
    dump a >  a;
    dump b >  b;
    dump b >  a;
    dump a >  b;
    dump a >= a;
    dump b >= b;
    dump b >= a;
    dump a >= b;
}
