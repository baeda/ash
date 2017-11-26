func main() : void
{
    { a : i32;          a = 0;         dump a;           }
    { a : i32;          a = 12 + 5;    dump a;           }
    { a : i32;          a = 12;        dump a + 5;       }
    { a : i32;          a = 12;        dump (a) + 5;     }
    { a : i32;          a = 12;        dump a + (5);     }
    { a : i32;          a = 12;        dump (a) + (5);   }
    { a : i32;          a = 12;        dump ((a) + (5)); }
    { a : i32;          a = 12;        dump (a + 5);     }
    { a : i32;          a = 12;        dump 5 + a;       }
    { a : i32;          a = 12;        dump (5) + a;     }
    { a : i32;          a = 12;        dump 5 + (a);     }
    { a : i32;          a = 12;        dump (5) + (a);   }
    { a : i32;          a = 12;        dump ((5) + (a)); }
    { a : i32;          a = 12;        dump (5 + a);     }
    { a : i32; b : i32; a = 12; b = 5; dump a + b;       }
}
