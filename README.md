# 🚽 Skibidi Programming Language v2.0

**Skibidi** is a modern, expressive programming language with Gen-Z inspired syntax that makes coding fun and engaging! Built in Java, Skibidi brings a fresh take on programming with its unique keywords and intuitive structure.

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher (for .jar version)
- Windows (for .exe version)
- macOS, or Linux (for .jar version)

### Running Skibidi Programs

#### Option 1: Using the Executable (.exe) - Windows Only
1. **Download** `SkibidiInterpreter.exe` from `https://drive.google.com/file/d/1GmuGYwEtPtEedpd-mDwiosLj4V6Z8Iiy/view?usp=sharing`
2. **Open** Command Prompt or PowerShell
3. **Run** your Skibidi program:

```cmd
SkibidiInterpreter.exe your_program.skibidi
```

#### Option 2: Using Java (.jar)
1. **Download** the compiled interpreter from the `/build/` folder
2. **Navigate** to your project directory
3. **Run** your Skibidi program:

```bash
java skibidi.Skibidi your_program.skibidi
```

#### Option 3: From Source
```bash
cd skibidi/
javac *.java
java skibidi.Skibidi your_program.skibidi
```ng Language


### Example Program

Create a file called `hello.skibidi`:

```skibidi
blud message = "Hello, Skibidi World!";
yap message;

blud x = 10;
blud y = 5;
yap x + y;
```

Run it:
```bash
SkibidiInterpreter.exe hello.skibidi
OR
java skibidi.Skibidi hello.skibidi
```

Output:
```
Hello, Skibidi World!
15
```

## 📚 Language Syntax

### 🔄 Skibidi vs Traditional Keywords

| Traditional | Skibidi | Purpose |
|------------|---------|---------|
| `var/let` | `blud` | Variable declaration |
| `print` | `yap` | Output/Print statement |
| `if` | `vibecheck` | Conditional statement |
| `else` | `vibecheckfail` | Else clause |
| `while` | `cook` | While loop |
| `for` | `rizzwalk` | For loop |
| `function` | `sauce` | Function declaration |
| `return` | `yeet` | Return statement |
| `try` | `pray` | Try block (error handling) |
| `catch` | `onL` | Catch block |
| `finally` | `gotchu` | Finally block |
| `break` | `ragequit` | Break statement |
| `continue` | `ghostnext` | Continue statement |
| `true` | `W` | Boolean true |
| `false` | `L` | Boolean false |
| `null` | `none` | Null value |

### Variables
Declare variables using the `blud` keyword:

```skibidi
blud name = "Skibidi";
blud age = 21;
blud isAwesome = W;
blud nothing = none;
```

### Data Types
- **Strings**: `"Hello World"`
- **Numbers**: `42`, `3.14`
- **Booleans**: `W` (true), `L` (false)
- **None**: `none` (equivalent to null)

### Output
Use `yap` to print values:

```skibidi
yap "Hello World";
yap 42;
yap W;
```

### Control Flow

#### If-Else Statements
```skibidi
blud score = 85;

vibecheck (score >= 60) {
    yap "Pass";
} vibecheckfail{
    yap "Fail";
} 
```

#### While Loops
```skibidi
blud count = 1;
cook (count <= 5) {
    yap "Count: " + count;
    count = count + 1;
}
```

#### For Loops
```skibidi
rizzwalk (blud i = 1; i <= 5; i = i + 1) {
    vibecheck (i == 3) {
        ghostnext; // continue
    }
    yap "Number: " + i;
}
```

### Functions
```skibidi
sauce greet(name) {
    yeet "Hello, " + name + "!";
}

blud message = greet("World");
yap message;
```

### Error Handling
```skibidi
pray {
    blud result = 10 / 0; // This will cause an error
    yap result;
} onL (error) {
    yap "Caught error: " + error;
} gotchu {
    yap "This always runs!";
}
```

### Expressions
Skibidi supports arithmetic and logical operations as well as ternary expressions:

```skibidi
blud result = 10 + 5 * 2;  // 20
blud greeting = "Hello" + " " + "World";  // "Hello World"
blud isAdult = age >= 18 ? W : L;  // Ternary-like expression
```

### Operators
- **Arithmetic**: `+`, `-`, `*`, `/`
- **Comparison**: `==`, `!=`, `<`, `<=`, `>`, `>=`
- **Logical**: `and`, `or`, `!`
- **Unary**: `-`, `!`

### Comments
```skibidi
// This is a single-line comment
blud x = 5; // Comments can also be at the end of lines
```

## 🎯 Current Features (v2.0)

- ✅ Variable declarations with `blud`
- ✅ Print statements with `yap`
- ✅ Control flow (`vibecheck`, `cook`, `rizzwalk`)
- ✅ Functions with `sauce` and `yeet`
- ✅ Error handling (`pray`, `onL`, `gotchu`)
- ✅ Loop control (`ragequit`, `ghostnext`)
- ✅ Arithmetic expressions
- ✅ String concatenation
- ✅ Boolean operations (`W`, `L`)
- ✅ Comparison operators
- ✅ Dynamic typing
- ✅ Recursive functions
- ✅ Local scopes and environments
- ✅ Comprehensive error reporting

## 🧪 Example Programs & Test Scripts

Run these programs to explore Skibidi's features:

```bash
# Basic language features
SkibidiInterpreter.exe examples/basic_test.skibidi

# Control flow (if-else, loops)
SkibidiInterpreter.exe examples/control_flow_test.skibidi

# Functions and recursion
SkibidiInterpreter.exe examples/function_test.skibidi

# Error handling
SkibidiInterpreter.exe examples/error_handling_test.skibidi

# Pattern generation
SkibidiInterpreter.exe examples/pattern.skibidi

```
- ✅ Ternary-like conditional expressions
- ✅ Basic REPL mode for interactive coding


## 📁 Project Structure

```
Jskibiddi/
├── build/                    # Compiled files (ready to run)
│   ├── skibidi/             # Java .class files
├── skibidi/                 # Source code
│   ├── Skibidi.java         # Main interpreter class
│   ├── Lexer.java           # Tokenizer
│   ├── Parser.java          # Syntax analyzer
│   ├── Interpreter.java     # Expression evaluator
│   └── ...
├── examples/                # Example programs & test scripts
└── README.md
```

## 🎮 Example Programs

### Basic Calculator
```skibidi
blud a = 15;
blud b = 7;

yap "Addition: " + (a + b);
yap "Subtraction: " + (a - b);
yap "Multiplication: " + (a * b);
yap "Division: " + (a / b);
```

### String Manipulation
```skibidi
blud firstName = "John";
blud lastName = "Doe";
blud fullName = firstName + " " + lastName;

yap "Full name: " + fullName;
yap "Name length: " + (firstName + lastName);
```

### Boolean Logic
```skibidi
blud isStudent = W;
blud hasJob = L;
blud needsScholarship = isStudent and !hasJob;

yap "Needs scholarship: " + needsScholarship;
```

### Advanced Examples

#### Fibonacci Calculator (examples/function_test.skibidi)
```skibidi
sauce fibonacci(n) {
    vibecheck (n <= 1) {
        yeet n;
    } vibecheckfail {
        yeet fibonacci(n - 1) + fibonacci(n - 2);
    }
}

rizzwalk (blud i = 0; i < 10; i = i + 1) {
    yap "fib(" + i + ") = " + fibonacci(i);
}
```

#### Pattern Generation (examples/pattern.skibidi)
```skibidi
yap "Basic Pattern Printer";
rizzwalk (blud i = 1; i <= 5; i = i + 1){
    blud r = "";
    rizzwalk (blud j = 1; j <= i; j = j + 1){
        r = r + j + " ";
    }
    yap r;
}
```

#### Error-Safe Calculator (examples/error_handling_test.skibidi)
```skibidi
sauce safeDivide(a, b) {
    pray {
        yeet a / b;
    } onL (error) {
        yap "Division error: " + error;
        yeet 0;
    }
}

yap safeDivide(10, 2);  // 5
yap safeDivide(10, 0);  // 0 (with error message)
```

**💡 Try all examples:** Check the `/examples/` folder for more comprehensive programs including control flow tests, function demonstrations, and error handling scenarios!

## 📄 License

This project is open source. Feel free to use, modify, and distribute according to the license terms.

---

**Happy coding with Skibidi! 🚽✨**
