# DeriveColors

### Install on MacOS

* Download and Install at least Java-17, e.g. from https://adoptium.net/
* Ensure that the new JDK is available on your command line.
* ensure a recent version of Maven is installed (3.8+)

```bash
# First list all installed JDKs - JDK17 should be listed there
% /usr/libexec/java_home -V

# In case multiple JDKs are installed, switch to 17.
% export JAVA_HOME=`/usr/libexec/java_home -v 17`

# clone the project from Github
% git clone https://github.com/Oliver-Loeffler/DeriveColorsFX.git
% cd DeriveColorsFX
% mvn package
% sh mac-package.sh
```

* `mac-package.sh` will execute `jlink` and `jpackage` so that eventually a DMG image will be available in `target/mac-installer`.