func simple_branch_true() : void
{
    a : i32;
    if (true) {
        a = 1;
    } else {
        a = 0;
    }
    dump a;
}

func simple_branch_false() : void
{
    a : i32;
    if (false) {
        a = 1;
    } else {
        a = 0;
    }
    dump a;
}

func main() : void
{
    simple_branch_true();
    simple_branch_false();
}
