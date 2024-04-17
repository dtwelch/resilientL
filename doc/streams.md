## Parsing phases 

The parsing process is conducted in two phases. In phase one, the `file(..)`
method transforms the token stream produced by the lexer into a flat list 
of events. The second phase, the parser class' `buildTree(..)` function creates the 
actual tree structure from this flat list of events. 

#### Example in `parse(..)`:
Here is the input string passed into `parse`:
```rust
fn f()
```
it gets chunked up into a `Vector<Token>` by the lexer class and looks like:
```java
// the list on the rhs is returned by Lexer.lex(text);
var tokens = [{kind=FnKeyword, text="fn"}, 
              {kind=Name, text="f"}, 
              {kind=LParen, text="("}, 
              {kind=RParen, text=")"}
```
Then this gets passed into the constructor of the parser.. (instantiated in 2nd line of `parse(..)`) 
```java
var p = new Parser(tokens);
```
Then `file(p)` is invoked which performs the first pass of the parse in which
the tokenstream is convered into parse 'events'. 

The call to `file(p)` sideeffect's parser `p`'s (initially-empty) list of `Vector<Parser.Event> events` objects -- fills it. 
Recall that parser `p` also stores the vector of tokens -- this will control the way 
the `file` method populates the `event` stream for the current program being parsed.
