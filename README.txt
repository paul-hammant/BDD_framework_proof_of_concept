Notable Shortcomings

1) There's no .story file handling or parsing
2) Scenario parsing only covers the G/W/T steps of a single scenario
  - crude implementation would need to be replaced
3) No meta-filter on scenario (but is on story)
4) No multi-threaded of Future based execution
  - should be easy though.

Differences to JB 3.x

5) Before and After are a chain of before+after pairs (either side of the pair is optional).
6) Meta filtering successfully excludes Before/AfterStory as well as Story
7) Steps collecting work on classes, not instances of steps POJO
   - one test proves the chronalogically later instantiation (or getting) of an instance to invoke the methods of
8) All the model objects are fully re-entrant
9) Execution state is modelled with a single boolean return value, and the stored invocations into a Monitor
   - the monitor would do all of the output formats