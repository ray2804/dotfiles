# POSIX
#
# Create option variables dynamically. Try call:
#
#    sh -x example.sh --verbose --test --debug

for i in "$@"
do
    case "$i" in
       --test|--verbose|--debug)
            shift                   # Remove option from command line
            name=${i#--}            # Delete option prefix
            eval "$name='$name'"    # make *new* variable
            ;;
    esac
done

echo "verbose: $verbose"
echo "test: $test"
echo "debug: $debug"
