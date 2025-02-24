
# Load Tester

BUILTIN_Load_Tester:
    type: command
    name: testload
    debug: false
    description: Tests server load.
    usage: /testload <&lt>power<&gt>
    allowed help:
    - determine <player.has_permission[denizen.testload]||<player.is_op||<context.server>>>
    permission: denizen.testload
    script:

    # double check the permission
    - if !<player.has_permission[denizen.testload]||<player.is_op||<context.server>>> queue clear

    - define n <context.args.get[1].as_int||null>

    # if no number specified, stress test with 100 queues
    - if <[n]> == null {
      - narrate 'Invalid integer, assuming 100.'
      - define n 100
      }

    # initialize the progress flag
    # this will count up each time a queue was completed
    - flag server stress_test_counter:0

    # keep track of the start time
    - define start_time <server.current_time_millis>

    # repeat the run command
    # each run will create a new queue, and increase the counter
    - repeat <[n]> {
      - run locally test instantly
      }

    # ...and end time, to compare against the start time
    - define end_time <server.current_time_millis>

    - narrate 'Completed <server.flag[stress_test_counter].as_int> of <[n]> total queues in <[end_time].sub_int[<[start_time]>].div[1000]> seconds.'

    # cleanup
    - flag server stress_test_counter:!


    test:
    - flag server stress_test_counter:++
