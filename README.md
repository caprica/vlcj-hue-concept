# vlcj Ambient Light Concept Demo

This project is a minimal proof of concept that uses a vlcj media player to play videos with some real-team analysis
that samples the video frames to derive sample colour values that can be sent via some API to external lighting.

The idea is that you partition the video surface into whatever shapes you want - rectangles are obviously pretty easy,
and this demo simply uses the four rectangle quadrants to simulate two lights in front (left and right) and two lights
beind (left and right).

In principle, any number and types of shapes could be used to simulate different physical light arrangements.

## Limitations

There are some limitations that a "real" application would need to address, but what is presented here is good enough
for its intended purpose as a proof of concept.

### RGB Colour Averaging Algorithm

The algorithm used to sample the RGB is pretty basic - it operates in one of two ways:

 1. Calculate the average (mean value) of the sum of squares of all the RGB component values from the original image
 2. Same as #1, but using the plain average (i.e. not using squares)

There may be faster, smarter, nicer algorithms.

### Optimisation

The implementation is naive and not necessarily efficient, there is plenty of room for optimising things.

### Painter vs Callback

This implementation hooks into vlcj's `CallbackImagePainter`, it will probably be more efficient to hook into the render
callback mechnaism earlier by using a custom 'RenderCallback' instead.

### Philips Hue

This example includes an interface to a Philips Hue bridge - if you want to try that you need to do some developer
setup on your bridge first - check the official documentation for how to do that.

Philips Hue lighting does not reproduce the full gamut of RGB values, this could be mitigated somewhat by using a
smarter algorithm to map RGB values to the available colour-space on Hue.

This example does not adjust the brightness of the lights, and indeed blacks do not turn the light off, but all of that
could be done with more work.

There may be some latency involved when setting a new colour value for the lights, and hardware API invocations may need
to be rate-limited anyway to prevent overwhelming the Hue Bridge. Nevertheless in practice this all actually works
quite well.

## Next Steps

You are encouraged to take this concept and make something awesome out of it, the only requirement is that you report
back what you've done, I'd love to see it.

## Requirements

This is a very rough and ready project at the moment.

It uses some dependencies that are in projects not deployed to Maven Central - so you will need to import these into
your own workspace, at least for now:

 * [vlcj-5.0.0-SNAPSHOT](https://github.com/caprica/vlcj)
 * [brue-1.0.0-SNAPSHOT](https://github.com/caprica/brue)
 * [brue-bridge-1.0.0-SNAPSHOT](https://github.com/caprica/brue-bridge)
 * [brue-okhttp-1.0.0-SNAPSHOT](https://github.com/caprica/brue-okhttp)

## Demo

Some YouTube videos showing the proof of concept:

 * https://www.youtube.com/watch?v=SNLN9uvkpwc
 * https://www.youtube.com/watch?v=FwEeiP8P0XM
