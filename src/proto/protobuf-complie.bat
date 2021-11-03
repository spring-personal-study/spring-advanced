cd $(dirname $0)
SRC_DIR=.
DST_DIR=../main/java

./protoc -I$SRC_DIR --java_out=$DST_DIR $SRC_DIR/person.proto

@REM ./protoc -I. --java_out=../ person.proto
@REM ./protoc -I. --java_out=../main/java ./person.proto
