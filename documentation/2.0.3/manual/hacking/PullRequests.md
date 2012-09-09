<!-- translated -->
<!--
# Pull requests, and Play!

Here are some guidelines for submitting pull requests to the Play project (and why your request might not be accepted).

The reason we've written it is that we've had to reject a bunch of requests recently - not because they're not good - just because they don't match our current project priorities. Each request needs to be assessed and managed appropriately, and if you look at the commit logs you'll see we're stupidly busy at the moment and need to make some tough decisions on where our time goes. 
-->
# プル・リクエストと Play

このページは、 Play へプル・リクエストを送信する際のガイドライン（および、どのようなプル・リクエストがマージされるかの説明）です。

これを書いているのは、最近、大量のプル・リクエストをリジェクトしなければならなかったです。その理由は、プル・リクエストの品質の問題ではなく、単に現在のプロジェクトの優先順位にそぐわない、ということでした。全てのプル・リクエストは適切に評価され、そして対応されます。しかし、Play のコミット・ログをご覧になった方はご存知かもしれませんが、Play のコア・メンバーは現在かなり多忙で、何にどれだけの時間を割くかを慎重に考えなければなりません。

<!--
## A moving target

The Play2.0 framework is under heavy active development by the core team, and the codebase is changing rapidly. Small fixes and changes for bugs without tickets are a very low priority at the moment (though this will obviously change once the framework become more mature). Pull requests for minor issues - especially for things like cleaning whitespace, indenting, or fixing minor typographical issues will most likely be rejected.
-->
## 動く標的

Play 2.0 フレームワークは現在、コア・チームにより活発に開発されている最中で、コードベースも急激に変化します。そのため、チケット無しのバグに対する小さな修正や変更は、今のところかなり優先度が低くなっています（もちろん、フレームワークがもっと成熟すると、この方針は変わるでしょう）。小さな問題に対するプル・リクエストー例えば、スペース、インデント、軽微な誤字・脱字の修正などーは、かなりの確率でリジェクトされます。

<!--
## Features are forever 

Unfortunately, pull requests that add new features to Play are also likely to be rejected. A framework needs to fit the majority of cases, and can never cater for every situation: features that are absolutely essential to one team, will be redundant bloat to another. Additionally, any code that is merged into Play needs to be supported for a long long time. Adding support for something means maintaining, testing, and updating the related code throughout the framework's life - which requires resources from other parts of the project.

Any decision to add or remove even a minor feature has serious consequences and has to be considered carefully and thoroughly.
-->
## 新機能は限りなく

残念ながら、Play に新機能を追加するようなプル・リクエストもリジェクトされやすいでしょう。フレームワークは大多数の用途にフィットするように作られる必要があります。言い換えれば、全てシチュエーションを満たすことはできないということです。ある１チームでのみ絶対に必要な機能があるとして、その機能はその他の全てのユーザにとっては余分な機能になります。さらに、一旦 Play にマージされた機能は、長い長い期間、サポートされ続ける必要があります。何かのサポートをフレームワークに追加するということは、そのフレームワークの生涯に渡って、メンテナンス、テスト、関連コードのアップデートという作業が必要になるということを意味します。そのような作業にも、Play プロジェクトのリソースが必要です。

したがって、小さな機能の追加・削除であってもプロジェクトに深刻な影響を与える可能性があるため、慎重かつ先々のことを考えて実施しなければなりません。

<!--
## If you're keen to contribute

But good code is good code, and good ideas are good ideas. If you're serious about getting involved, you spot problems with code, or have a feature that you really think is missing (or shouldn't be present) then jump on the mailing lists and discuss it. We just don't want anyone to waste time creating some cool stuff that we can't use.
-->
## それでも熱意を持って貢献してくださる方へ

とはいっても、良いコードは良いコード、良いアイデアは良いアイデアです。真剣にプロジェクトに参加したい方、コードに問題を発見した方、絶対に必要な機能がフレームワークにない（または、存在すべきでない機能がフレームワークにある）と思われた方は、メーリングリストに飛び込んで、まずは話し合いましょう。私達は Play に関わる全員が、「クールだけど使われない機能」ではなく、本当に役に立つものだけに時間を使って欲しいと考えているだけなのです。