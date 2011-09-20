#
# Compile scala source code
#
echo 'Started compiling...'
scalac -d bin/ src/edu/nyu/hps/mint/*.scala
echo 'Finished compiling.'
