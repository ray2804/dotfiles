Ryan Neufeld:

> This is totally needed, and you're completely correct in your assessment; Pedestal does not have the answers yet for re-usable components and abstractions above the dataflow functions themselves.

I'm glad to learn that you guys also acknowledge a certain potential here, one which would easily pwn any out there (really, I think even in its current state as a newborn, Pedestal is much like the web frame analogy of the spartan baby: already chewing bubblegum and kicking ass in the uterus)

> We've started to think about this back on the ranch, but I still think people are still getting their feet wet with Pedestal. I imagine as we all start to notice patterns we'll have stronger ideas about how to architect a re-usable component system.

Good to hear also that people are taking time to let it sink in. I think the incremental development approach you've taken with versions of the model for one, is a good show of such an approach where, luckily, not often its too late to change your mind. Indeed the core abstractions would still remain messages. Unlikely major things around the exact format are going to change much (the protocols really) and as such indeed allows for decoupled development and components interoperability.

> Luckily, this can exist outside of the core of Pedestal, so others are free to experiment abstracting dataflow construction into libraries. I'm eagerly awaiting new plugins :D

Plugins? What do you constitute as a plugin, or does the pedestal-plugin-plugin still has to be written? My question really being: is there something like a plugin facility for Pedestal besides the compartmentalized regular abstractions I've encountered in the tutorial/samples?

> What would it look like to compose separate components into a dataflow? Would each component be their own map, merged together by a merge-fn with knowledge of how to collapse dataflows? What if I needed tighter control over how that dataflow came together? Would some components be hand written dataflow, and others simply function calls that returned populated dataflow snippets?

Exactly, this would be my point as well. We need a governing, overarching logic on how to work with trees of separate component transforms, emits and such and how these are to be merged with other trees etc.

On the other hand, we already have Google Closure web components and using anything less will cause compatibility issues in initial approach (of course you can always extern libraries etc. but is say a Sencha that much better than Google Closure? I say not. One thing that is major with these type of frameworks, is design/layout aesthetics. Google Closure is buttugly 1998 type widgets but, they should be easily styled to more modern varieties. The question is, do we have enough components at hand, or can we work with these to serve the majority of needs?

This is the way I more or less blurry see the whole thing: what are we trying to do new here exactly? We are talking about rendering capabilities on the front-end matter which can now be
done using HTML Pedestal templates or Hiccup if you like. But it's the reusable part they are missing. And it's not only structure, also style will become more and more a factor and bootstrap.css although being nice and all, isn't really fun to work with when you want to edit it to a customers preference/corporation style. I would usually pick the SCSS/Stylus forks and regenerate from those preprocessors because of the pain of manual operations.

Luckily we do have e.g. Garden for CSS in Clojure and a truly web UI component framework would allow for easily defining components in structure, behaviour AND style. Using syntax quoting we can, as with collapsing messages I guess, create templates for reuse. You should really checkout Backtick in case you haven't yet, I love it for these kind of things.

See, what I was missing initially, for example, although technically not front-end, was in the DataUI that if e.g. I made a node true value, and would ask for params dialog, I would have expected/liked to see a checkbox. It's that kind of smart which isn't built-in (yet).

The same could go for a lot of the front-end aspect rendering really because we should be able to easily say: "I want this node to take this information and create a component A for me here, and component B for me here. Since - say a list but any other data - often could be represented in multiple forms as far as the UI go, you would want to be able and on a finer-grained level be able and say "Well sure create a listbox of all these values, but although A and B are technically part of it, I don't want them to show up in the UI so these should be hidden". As a very trivial example of the types of issues you'll run into. Again, as it is now, you could write all these things in a Cljs rendering/template part manually, but to begin with, I think I'll take a macros that ease the burden of writing perfect goog.UI statisfactory Cljs because writing up a goog.Component doesn't seem like a very fun thing to do now.


> 
> On Jul 17, 2013, at 3:19 PM, Alexander Kiel <alexan...@gmx.net> wrote:
> Hi,
> 
> 
> I evaluate Pedestal for enterprise rich internet applications (RIA). We are currently using Sencha GXT [1]. As I like to apply a more functional approach to our team, Clojure and Pedestal are good candidates.
> 
> 
> I know that Pedestal is very new and I also think that Pedestals core abstractions will never touch GUI component models. I see Pedestal as a basic abstraction to handle state and events. A component model would lie on top of core Pedestal.
> 
> 
> An obvious question: Is someone working on such a component model/abstraction?
> 
> 
> In the last two days I played with forms and data grids. Even a simple form with validation and enabled/disabled states leads to quite big data models, many transforms and complex custom emitters.
> 
> 
> This thread can be a place for brainstorming abstractions needed for rich, reusable components.
> 
> 
> Regards
> Alex
> 
> 
> [1]: <http://www.sencha.com/products/gxt/> 
> 
> 
> 
> -- 
> 
> You received this message because you are subscribed to the Google Groups "pedestal-users" group.
> 
> To unsubscribe from this group and stop receiving emails from it, send an email to pedestal-user...@googlegroups.com.
> 
> Visit this group at http://groups.google.com/group/pedestal-users.a






I think I get what you mean. Of course, they can just maintain exactly that way as they were once built, optionally have different versions in profiles like clojure 1.2 1.3 etc is often enough seen around. Package them with their own libraries running stand-alone, should be pretty much fine, until perhaps breaking updates on native dependencies like log4j, jvm and what not. But I don't think that's even the question here. The question should probably be: *"What do we intend to use it for?"* and I don't think its supposed to ran as a uberjar but more so reflect some design concepts, different features of pedestal and how to possibly use it in a few limited setup scenarios. Those become stale by the very nature of the maturing framework. It's nice that there are still examples and artifacts from the Clojure 1.2 era and Leiningen 1, but I don't use those anymore, not for reference, or samples unless its a very attractive abandoned library (I had a few parsers, heuristics programs that were interesting to walk through) but they *needed* to be updated or they wouldn't run. Even with those profiles etc, I think you will still get a warning if you e.g. declare dynamic vars by using earmuffs without the `^:dynamic` tag or, I could be totally not getting the whole idea behind defining the different clojure versions in `project.clj` profiles maps. Either way, at least my machine, especially nowadays less and less stomachs old Clojure. Either it is leiningen, lighttable, clojure itself, Java, someone is always complaining and updating it fixes the problems nearly always. Anyway this is what you get in the climb to stability with any API, so I'd still be curious to learn if you e.g. had a special idea or policy/plan with regards to the release cycle and how/when to deprecate stuff, phase in and out components (especially the :version key had me wondering about the exact implication of explicit numbering the data flow with versions).

Now see, I hadn't looked at the templates just yet, I did already notice through those, that there had been a inc to version 0.2.1 though since I generate a few apps to play with every other day or so. What I'm less sure about though, is if I feel they should even be in the equation so much here. It might be, if they would be tutorial samples that one follows along, that they would need to keep these in sync as much as possible, match not only version numbers but of course especially with awareness of any deprecated functions you might be explaining that changed in the meanwhile, or use namespaces that got renamed at some point.

I think the most sense would make that you'd reflect at least the most significant changes. If they would be slightly off by a minor/patch, it's not so much an issue and when you've missed a breaking change and this is still present in the samples, documents, tutorials basically using some outdated methods. Again, these problems will in time go away as the API stabilizes and should there be many parts of the API, even classified per namespace(s) as frozen, experimental, stable like node.js does with the internal modules
