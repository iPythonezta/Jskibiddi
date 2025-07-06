# Skibidi Programming Language

**Skibidi** is a modern, expressive programming language with Gen-Z inspired syntax that makes coding fun and engaging! Built in Java, Skibidi brings a fresh take on programming with its unique keywords and intuitive structure.

## Quick Start

### Prerequisites
- Java 8 or higher
- Windows, macOS, or Linux

### Running Skibidi Programs

1. **Download** the compiled interpreter from the `/build/` folder
2. **Navigate** to your project directory
3. **Run** your Skibidi program:

```bash
java skibidi.Skibidi your_program.spp
```

### Example Program

Create a file called `hello.spp`:

```skibidi
blud message = "Hello, Skibidi World!";
yap message;

blud x = 10;
blud y = 5;
yap x + y;
```

Run it:
```bash
java skibidi.Skibidi hello.spp
```

Output:
```
Hello, Skibidi World!
15
```

## 📚 Language Syntax

### Variables
Declare variables using the `blud` keyword:

```skibidi
blud name = "Skibidi";
blud age = 21;
blud isAwesome = true;
blud nothing = none;
```

### Data Types
- **Strings**: `"Hello World"`
- **Numbers**: `42`, `3.14`
- **Booleans**: `true`, `false`
- **None**: `none` (equivalent to null)

### Output
Use `yap` to print values:

```skibidi
yap "Hello World";
yap 42;
yap true;
```

### Expressions
Skibidi supports arithmetic and logical operations as well as ternary expressions:

```skibidi
blud result = 10 + 5 * 2;  // 20
blud greeting = "Hello" + " " + "World";  // "Hello World"
blud isAdult = age >= 18 ? true : false;  // Ternary-like expression
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

## 🎯 Current Features (v1.0)

- ✅ Variable declarations with `blud`
- ✅ Print statements with `yap`
- ✅ Arithmetic expressions
- ✅ String concatenation
- ✅ Boolean operations
- ✅ Dynamic typing
- ✅ Expression evaluation
- ✅ Error handling and reporting
- ✅ Ternary-like conditional expressions
- ✅ Basic REPL mode for interactive coding

## 🔮 Upcoming Features (v2.0+)
- 🧩 Conditional statements 
- 🔁 Loops
- 🧮 Functions


### Advanced Features (v3.0+)
- 🔄 Inheritance with `super`
- 📦 Modules and imports
- 🔧 Standard library functions


## 🛠️ Building from Source

If you want to modify or contribute to Skibidi:

1. **Clone** the repository
2. **Navigate** to the `skibidi/` folder
3. **Compile** the Java files:
   ```bash
   javac *.java
   ```
4. **Run** your program:
   ```bash
   java skibidi.Skibidi your_program.spp
   ```

## 📁 Project Structure

```
Jskibiddi/
├── build/                 # Compiled .class files (ready to run)
│   └── skibidi/
├── skibidi/              # Source code
│   ├── Skibidi.java      # Main interpreter class
│   ├── Lexer.java        # Tokenizer
│   ├── Parser.java       # Syntax analyzer
│   ├── Interpreter.java  # Expression evaluator
│   └── ...
├── examples/             # Example Skibidi programs
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
blud isStudent = true;
blud hasJob = false;
blud needsScholarship = isStudent and !hasJob;

yap "Needs scholarship: " + needsScholarship;
yap (2 > 3 ? "True" : "False");
```

---

**Happy coding with Skibidi! 🚽✨**

*Remember: In Skibidi, every variable is a "blud" and every output is a "yap"!*
