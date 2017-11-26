func add(x : i32, y : i32) : i32
{
    return x + y;
}

func main() : void
{
    dump add(2, 3);

    a : i32;
    a = 2;
    dump add(a, 3);

    b : i32;
    b = 3;
    dump add(a, b);
}
