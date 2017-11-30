func fibonacci(n : u64) : u64
{
    if (n <= 1) {
        return n;
    }

    res  : u64;
    prev : u64;
    i    : u64;

    res  = 1;
    prev = 1;
    i    = 2;

    while (i < n) {
        tmp : u64;

        tmp = res;
        res = res + prev;
        prev = tmp;

        i = i + 1;
    }

    return res;
}

func main() : void
{
    dump fibonacci( 0);
    dump fibonacci( 1);
    dump fibonacci( 2);
    dump fibonacci( 3);
    dump fibonacci( 4);
    dump fibonacci( 5);
    dump fibonacci( 6);
    dump fibonacci( 7);
    dump fibonacci( 8);
    dump fibonacci( 9);
    dump fibonacci(10);
    dump fibonacci(20);
    dump fibonacci(30);
    dump fibonacci(40);
}
