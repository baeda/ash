func main() : void
{
    { a : i8;  b : i8;  a = 1; b = 2; dump a + b; }
    { a : i16; b : i16; a = 1; b = 2; dump a + b; }
    { a : i32; b : i32; a = 1; b = 2; dump a + b; }
    { a : i64; b : i64; a = 1; b = 2; dump a + b; }
    { a : u8;  b : u8;  a = 1; b = 2; dump a + b; }
    { a : u16; b : u16; a = 1; b = 2; dump a + b; }
    { a : u32; b : u32; a = 1; b = 2; dump a + b; }
    { a : u64; b : u64; a = 1; b = 2; dump a + b; }
    { a : bool; a = true;             dump a;     }
    { a : bool; a = false;            dump a;     }

    { a : i8  = 1; b : i8  = 2; dump a + b; }
    { a : i16 = 1; b : i16 = 2; dump a + b; }
    { a : i32 = 1; b : i32 = 2; dump a + b; }
    { a : i64 = 1; b : i64 = 2; dump a + b; }
    { a : u8  = 1; b : u8  = 2; dump a + b; }
    { a : u16 = 1; b : u16 = 2; dump a + b; }
    { a : u32 = 1; b : u32 = 2; dump a + b; }
    { a : u64 = 1; b : u64 = 2; dump a + b; }
    { a : bool = true;          dump a;     }
    { a : bool = false;         dump a;     }
}
