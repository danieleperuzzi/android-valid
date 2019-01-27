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
            - [Using SortedConstraintSetFactory](#Using-SortedConstraintSetFactory)
        - [Validating](#Validating)
    - [Bulk validation](#Bulk-validation)
        - [Declaring BulkValidator](#Declaring-BulkValidator)
        - [Preparing SortedConstraintSet map](#Preparing-SortedConstraintSet-map)
        - [Validating all](#Validating-all)
    - [Observing validation](#Observing-validation)
        - [Preparing validator and SortedConstraintSet map](#Preparing-validator-and-SortedConstraintSet-map)
        - [Declaring ValidatorObserver](#Declaring-ValidatorObserver)
        - [Validating with observer](#Validating-with-observer)
- [Extending the library](#Extending-the-library)
    - [Creating new Validable](#Creating-new-Validable)
    - [Creating new Constraint](#Creating-new-Constraint)

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
    - [optional: use SortedConstraintSetFactory](#Using-SortedConstraintSetFactory)
- [validate](#Validating)

```java
Validator validator = new SingleThreadValidator();
Validable<String> validable = new ValidableText("Lorem ipsum", TAG);

SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
                .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
                .build();

validator.validate(validable, constraintSet, new Validator.Callback() {
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

You can also use directly the **BaseValidator** class passing to it an Executor, a
ValidatorAlgorithmFactory and a main thread Handler.

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
SortedConstraintSet constraintSet = new SortedConstraintSet.Builder()
                .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
                .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
                .build();
```

The validator is fed with **SortedConstraintSet** that simply is a set of constraint ordered
in a certain manner and it processes them one by one following that order.

Every constraint takes three parameters:
- the value that is compared to the validable value
- the priority, lower it is then it is processed first
- the error message thrown when the constraint is not satisfied. It is also possible
to pass a map if multiple errors are thrown

In this case we are telling to the validator that the string should first not
be empty and then its minimum length should be 6 characters.

When any constraint is added to the SortedConstraintSet then it is automatically ordered.

##### Using SortedConstraintSetFactory
It may happen that the same objects must be validated in different sections of the app so,
in order to make code clearer, create all the SortedConstraintSet for each object and retrieve
them when necessary with **SortedConstraintSetFactory**.

```java
String usernameRegex = //put your regex here
String USERNAME_TAG = //tag for the username

String passwordRegex = //put your regex here
String PASSWORD_TAG = //tag for the password

SortedConstraintSet usernameConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

SortedConstraintSet passwordConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<String, SortedConstraintSet> constraintSetMap = new HashMap<>();
constraintSetMap.put(USERNAME_TAG, usernameConstraintSet);
constraintSetMap.put(PASSWORD_TAG, passwordConstraintSet);

SortedConstraintSetFactory factory = new SortedConstraintSetFactory(constraintSetMap);
```

Later just retrieve the desidered SortedConstraintSet.

```java
SortedConstraintSet usernameConstraintSet = factory.getConstraintSetByTag(USERNAME_TAG);
```

#### Validating

```java
validator.validate(validable, constraintSet, new Validator.Callback() {
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

###### Tip
If the constraint is only one you can skip the previous [step](#Choosing-constraints)
passing it directly to the validator

```java
Constraint<String, Integer> constraint = new MinLengthTextConstraint(6, 1, "minimum length is 6");

validator.validate(validable, constraint, new Validator.Callback() {
            @Override
            public void status(Validable<?> value, ValidatorResult result) {
                //get the result of the validation
            }
        });
```

### Bulk validation
```java
Validator validator = new SingleThreadValidator();
CollectionValidator collectionValidator = new BulkValidator(validator);

Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

SortedConstraintSet usernameConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

SortedConstraintSet passwordConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap = new HashMap<>();
constraintSetByValidableMap.put(username, usernameConstraintSet);
constraintSetByValidableMap.put(password, passwordConstraintSet);

collectionValidator.validateCollection(constraintSetByValidableMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validatorResultByValidableMap, ValidableCollectionStatus status) {

    }
});
```

Sometimes is useful to validate a set of validables in one shot, collectionValidator
aims to do this.

- [declare BulkValidator](#Declaring-BulkValidator)
- [Prepare SortedConstraintSet map](#Preparing-SortedConstraintSet-map)
- [validate all](#validating-all)

#### Declaring BulkValidator
```java
Validator validator = new SingleThreadValidator();
CollectionValidator collectionValidator = new BulkValidator(validator);
```
See [declaring Validator](#Declaring-Validator) for further info. BulkValidator internally use a validator to do operations.

#### Preparing SortedConstraintSet map
```java
Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

SortedConstraintSet usernameConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

SortedConstraintSet passwordConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap = new HashMap<>();
constraintSetByValidableMap.put(username, usernameConstraintSet);
constraintSetByValidableMap.put(password, passwordConstraintSet);
```

See [declaring Validable](#Declaring-Validable) and [choosing constraints](#Choosing-constraints)
to know about them. Once done with those steps just create a map to associate
every validable to its constraint set.

#### Validating all
```java
collectionValidator.validateCollection(constraintSetByValidableMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validatorResultByValidableMap, ValidableCollectionStatus status) {
        //get the result of the bulk validation
    }
});
```

Validate all the validables in one shot and wait for the result that is a map
of <Validable, ValidatorResult>, to know everything about every validable,
and a synthetic global status that is either ALL_VALID or AT_LEAST_ONE_NOT_VALID.

### Observing validation
```java
Validator validator = new SingleThreadValidator();

Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

SortedConstraintSet usernameConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

SortedConstraintSet passwordConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap = new HashMap<>();
constraintSetByValidableMap.put(username, usernameConstraintSet);
constraintSetByValidableMap.put(password, passwordConstraintSet);

ValidatorObserver observer = new ValidatorObserver(constraintSetByValidableMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validatorResultByValidableMap, ValidableCollectionStatus status) {
        //get informations about the global state of the observed validables every time one of them is validated
    }
});

validator.validate(username, usernameConstraintSet, observer, new Validator.Callback() {
    @Override
    public void status(Validable<?> value, ValidatorResult result) {
        //get the result of the single validation
    }
});
```

- [Prepare validator and SortedConstraintSet map](#Preparing-validator-and-SortedConstraintSet-map)
- [declare ValidatorObserver](#Declare-ValidatorObserver)
- [Validate with observer](#Validating-with-observer)

#### Preparing validator and SortedConstraintSet map
```java
Validator validator = new SingleThreadValidator();

Validable<String> username = new ValidableText("John123", "username");
Validable<String> password = new ValidableText("password1234", "password");

String usernameRegex = //put your regex here
String passwordRegex = //put your regex here

SortedConstraintSet usernameConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(20, 2, "maximum length is 20"))
        .addConstraint(new RegexTextConstraint(usernameRegex, 3, "username must contain only letters"))
        .build();

SortedConstraintSet passwordConstraintSet = new SortedConstraintSet.Builder()
        .addConstraint(new MandatoryTextConstraint(true, 0, "mandatory field"))
        .addConstraint(new MinLengthTextConstraint(6, 1, "minimum length is 6"))
        .addConstraint(new MaxLengthTextConstraint(10, 2, "maximum length is 10"))
        .addConstraint(new RegexTextConstraint(passwordRegex, 3, "password must contain only letters"))
        .build();

Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap = new HashMap<>();
constraintSetByValidableMap.put(username, usernameConstraintSet);
constraintSetByValidableMap.put(password, passwordConstraintSet);
```

See [declaring Validator](#Declaring-Validator) and [preparing SortedConstraintSet map](#Preparing-SortedConstraintSet-map)

#### Declaring ValidatorObserver
```java
ValidatorObserver observer = new ValidatorObserver(constraintSetByValidableMap, new CollectionValidator.Callback() {
    @Override
    public void status(Map<Validable<?>, ValidatorResult> validatorResultByValidableMap, ValidableCollectionStatus status) {
        //get informations about the global state of the observed validables every time one of them is validated
    }
});
```

Instantiate a new ValidatorObserver passing to it the previously created
<Validable, SortedConstraintSet> map so it is aware of the validables that
should be observed: every time one of them is being validated then the
observer update the global status of the validable set and invoke the callback
that gives precise informations about all the validables and a
synthetic status that is either ALL_VALID or AT_LEAST_ONE_NOT_VALID.

#### Validating with observer
```java
validator.validate(username, usernameConstraintSet, observer, new Validator.Callback() {
    @Override
    public void status(Validable<?> value, ValidatorResult result) {
        //get the result of the single validation
    }
});
```

Validator.validate accepts one more parameter that is the observer that get
informed about every validation done by the validator. See also [Validating](#Validating).

## Extending the library
This library is intended to be used to validate any kind of object with any kind of
constraint so if some classes aren't provided it is also very easy to write them.

- [create new Validable](#Creating-new-Validable)
- [create new Constraint](#Creating-new-Constraint)

### Creating new Validable

```java
public interface Validable<V> {
    V getValue();
    String getTag();
    void setValue(V value, @Nullable String tag);
}
```

Just implement the Validable interface. V represents the type of the object that should be
validated.<br/>
The TAG is useful in some situations like when validation ends and you want to know what object
has been validated.

### Creating new Constraint

```java
public abstract class Constraint<V, C> implements Comparable<Constraint> {

    ...

    protected Constraint(C constraint, int evaluationPriority, String error) {
        ...
    }

    protected Constraint(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        ...
    }

    ...

    protected abstract ConstraintResult evaluate(V value);
    protected abstract boolean shouldStopValidation(V value);

    ...
}
```

Extends this base Class. V has the same meaning as explained [before](#creating-new-Validable), C represents the object
holding the constraint, String, Integer, whatever that is necessary. Be careful about calling
super inside the constructors.

**NOTE**<br/>
If it is provided a value of different type of the one that the constraint
can validate then a ClassCastException is thrown at runtime.

- **extend Constraint Class**
    ```java
    public MyConstraint<String, Integer> extends Constraint {

    ...

    }
    ```

    The first generic type is the one of the object being validated, the second is the type
    of the constraint.

- **call super in constructors**
    ```java
    protected MyConstraint(Integer constraint, int evaluationPriority, String error) {
        super(constraint, evaluationPriority, error);

        // do whatever you want
    }

    protected MyConstraint(Integer constraint, int evaluationPriority, Map<String, String> errorMap) {
        super(constraint, evaluationPriority, errorMap);

        // do whatever you want
    }
    ```

    The two constructors have in common:
     - **constraint:** the object that hold the constraint that should be compared to the value
     - **evaluationPriority:** the priority used by the SortedConstraintSet to order all the constraint

     instead are mutual exclusive **error** and **errorMap**, the first is used when only one error
     is thrown, the other when are necessary multiple errors.

- implement **protected ConstraintResult evaluate(String value);**
    ```java
    protected ConstraintResult evaluate(String value) {
        boolean valid;
        ConstraintResult result;
        Integer constraint = getConstraint();

        //compare value and constraint

        if(valid) {
            result = new ConstraintResult(ValidableStatus.VALID, null);
        } else {
            result = new ConstraintResult(ValidableStatus.NOT_VALID, getError());
        }

        return result;
    }
    ```

    this is the method where value and constraint are compared to determine if the first
    is compliant and should be declared VALID or NOT_VALID.

    **Tip**<br/>
    If there are multiple possible errors just use getErrorMap() method.


- implement **protected boolean shouldStopValidation(String value);**
    ```java
    protected boolean shouldStopValidation(String value) {
        boolean shouldStopValidation;

        // check the actual state of the value

        return shouldStopValidation;
    }
    ```

    There are some cases when, after a positive evaluation, it is not necessary anymore
    to continue validation regardless the next constraints, the validable is considered
    valid.
