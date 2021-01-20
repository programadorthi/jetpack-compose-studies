# jetpack-compose-studies
Project to study Jetpack Compose with subscribers on Twitch lives.

# Things we had discovered in our study sections
1. Modifiers order is very important. Changing the order can create unexpected behaviors;
2. `RectangleShape` is the default shape in some modifiers;
3. By default compose nodes consumes minimum space as possible. If you needs increase width/height you have to do that using a modifier as `fillMaxSize`;
4. Applying `border` modifier also do a `clip`. So we don't need to apply a clip before apply a border;
5. Modifiers can be combined using `then()` or operator `+` creating a chain of modifiers. This is a good expansion between public and private applied modifiers.
6. There is two ways to load an image resource. 
  - `imageResource` load image in a synchronous way. Easy to use.
  - `loadImageResource` is the asynchronous version. More verbose.
7. 
