Due to a bug in the way the gesture editor stores the date in the
file we need to strip the date line out. Use the following commands to
remove the data.

sed "/creation/d" core-gestures.gsa > a
mv a core-gestures.gsa
