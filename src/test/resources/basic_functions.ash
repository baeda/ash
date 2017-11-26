func declared_before_call() : i32
{
    return 84;
}

func main() : void
{
    dump declared_before_call();
    dump declared_after_call();
}

func declared_after_call() : i32
{
    return rnd() + 30;
}

func rnd() : i32
{
    return 12;
}
