# android-valid
This library allows you to do simple and easy field validation on your Android App.

It is designed to valid any kind of object against any kind of constraint.
Some example classes are provided and they are ready to use to validate text.

It is also very easy to provide custom implementation to add
new constraint or to validate different objects.

## Table of contents
- [Core features](#Core-features)
- [Examples](#Examples)
    - [Single validation](#Single-validation)
        - [Declaring Validator](#Declaring-Validator)
        - [Declaring Validable](#Declaring-Validable)
        - [Choosing constraints](#Choosing-constraints)
        - [Validating](#Validating)
    - [Bulk validation](#Bulk-validation)
        - [Declaring BulkValidator](#Declaring-BulkValidator)
        - [Preparing ValidatorOptions map](#Preparing-ValidatorOptions-map)
        - [Validating all](#Validating-all)
    - [Observing validation](#Observing-validation)
        - [Preparing validator and validator options map](#Preparing-validator-and-validator-options-map)
        - [Declaring ValidatorObserver](#Declaring-ValidatorObserver)
        - [Validating with observer](#Validating-with-observer)

## Core features
- single object validation
- multiple object validation in one shot
- ability to observe a set of object when validating one of them

Every object that is being validated goes through a process that involves
multiple ordered checks. Only if all the checks are positive the validation
succeeded.

## Examples
For sake of simplicity these examples use strings as objects to
be validated.

### Single validation
- [declare Validator](#Declaring-Validator)
- [declare Validable](#Declaring-Validable)
- [choose constraints](#Choosing-constraints)
- [validate](#Validating)

```java
Validator validator = new SingleThreadValidator();
Validable<String> validable = new ValidableText("Lorem ipsum", TAG);

ValidatorOptions options = new ValidatorOptions.Builder()
                .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
                .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
                .build();

validator.validate(validable, options, new Validator.Callback() {
            @Override
            public void status(Validable<?> value, ValidatorResult result) {
                //get the result of the validation
            }
        });
```

#### Declaring Validator

```java
Validator validator = new SingleThreadValidator();
```
There are three custom Validator implementation:

- **MainThreadValidator:** validation is done on the main thread
- **SingleThreadValidator:** validation is done on a single worker thread
- **PoolThreadValidator:** validation is done on a worker thread taken from
a thread pool

#### Declaring Validable

```java
Validable<String> validable = new ValidableText("Lorem ipsum", TAG);
```
Instantiate a new validable that can hold string object. It is possible to
add a tag that identifies the string being validated.
In case of multiple validation of the same string use the set method of the
validable

```java
validable.setValue("Lorem ipsum", TAG);
```
TAG can be null.

#### Choosing constraints

```java
ValidatorOptions options = new ValidatorOptions.Builder()
                .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
                .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
                .build();
```

The validator is fed with ValidatorOptions that simply is a list of constraint ordered
in a certain manner and it processes them one by one following that order.

Every constraint takes three parameters:
- the value that is compared to the validable value
- the priority, lower it is then it is processed first
- the error message thrown when the constraint is not satisfied. It is also possible
to pass a map if multiple errors are thrown

In this case we are telling to the validator that the string should first not
be empty and then its minimum length should be 7 characters.

When building the ValidatorOptions then constraint are automatically ordered.

#### Validating

```java
validator.validate(validable, options, new Validator.Callback() {
            @Override
            public void status(Validable<?> value, ValidatorResult result) {
                //get the result of the validation
            }
        });
```

Just start the validation and wait to get notified through the supplied callback.
The result encapsulates two informations:

- the status, it can be VALID or NOT_VALID
- the error message, if any

### Bulk validation
```java
Validator validator = new SingleThreadValidator();
CollectionValidator collectionValidator = new BulkValidator(validator);

Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

ValidatorOptions usernameOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

ValidatorOptions passwordOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, ValidatorOptions> optionsMap = new HashMap<>();
optionsMap.put(username, usernameOptions);
optionsMap.put(password, passwordOptions);

collectionValidator.validateCollection(optionsMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validableResults, ValidableCollectionStatus status) {

    }
});
```

Sometimes is useful to validate a set of validables in one shot, collectionValidator
aims to do this.

- [declare BulkValidator](#Declaring-BulkValidator)
- [Prepare ValidatorOptions map](#Preparing-ValidatorOptions-map)
- [validate all](#validating-all)

#### Declaring BulkValidator
```java
Validator validator = new SingleThreadValidator();
CollectionValidator collectionValidator = new BulkValidator(validator);
```
See [declaring Validator](#Declaring-Validator) for further info. BulkValidator internally use a validator to do operations.

#### Preparing ValidatorOptions map
```java
Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

ValidatorOptions usernameOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

ValidatorOptions passwordOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, ValidatorOptions> optionsMap = new HashMap<>();
optionsMap.put(username, usernameOptions);
optionsMap.put(password, passwordOptions);
```

See [declaring Validable](#Declaring-Validable) and [choosing constraints](#Choosing-constraints)
to know about them. Once done with those steps just create a map to associate
every validable to its options.

#### Validating all
```java
collectionValidator.validateCollection(optionsMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validableResults, ValidableCollectionStatus status) {
        //get the result of the bulk validation
    }
});
```

Validate all the validables in one shot and wait for the result that is a map
of Validable <==> ValidatorResult, to know everything about every validable,
and a synthetic global status that is either ALL_VALID or AT_LEAST_ONE_NOT_VALID.

### Observing validation
```java
Validator validator = new SingleThreadValidator();

Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

ValidatorOptions usernameOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

ValidatorOptions passwordOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, ValidatorOptions> optionsMap = new HashMap<>();
optionsMap.put(username, usernameOptions);
optionsMap.put(password, passwordOptions);

ValidatorObserver observer = new ValidatorObserver(optionsMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validableResults, ValidableCollectionStatus status) {
        //get informations about the global state of the observed validables every time one of them is validated
    }
});

validator.validate(username, usernameOptions, observer, new Validator.Callback() {
    @Override
    public void status(Validable<?> value, ValidatorResult result) {
        //get the result of the single validation
    }
});
```

- [Prepare validator and validator options map](#Preparing-validator-and-validator-options-map)
- [declare ValidatorObserver](#Declare-ValidatorObserver)
- [Validate with observer](#Validating-with-observer)

#### Preparing validator and validator options map
```java
Validator validator = new SingleThreadValidator();

Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

ValidatorOptions usernameOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

ValidatorOptions passwordOptions = new ValidatorOptions.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, ValidatorOptions> optionsMap = new HashMap<>();
optionsMap.put(username, usernameOptions);
optionsMap.put(password, passwordOptions);
```

See [declaring Validator](#Declaring-Validator) and [preparing ValidatorOptions map](#Preparing-ValidatorOptions-map)

#### Declaring ValidatorObserver
```java
ValidatorObserver observer = new ValidatorObserver(optionsMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validableResults, ValidableCollectionStatus status) {
        //get informations about the global state of the observed validables every time one of them is validated
    }
});
```

Instantiate a new ValidatorObserver passing to it the previously created
<Validable, ValidatorOptions> map so it is aware of the validables that
should be observed: every time one of them is being validated then the
observer update the global status of the validable set and invoke the callback
that gives precise informations about all the validables and a
synthetic status that is either ALL_VALID or AT_LEAST_ONE_NOT_VALID.

#### Validating with observer
```java
validator.validate(username, usernameOptions, observer, new Validator.Callback() {
    @Override
    public void status(Validable<?> value, ValidatorResult result) {
        //get the result of the single validation
    }
});
```

Validator.validate accepts one more parameter that is the observer that get
informed about every validation done by the validator. See also [Validating](#Validating).