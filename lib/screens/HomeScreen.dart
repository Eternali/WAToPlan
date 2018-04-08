import 'package:flutter/material.dart';

import 'package:watoplan/routes.dart';
import 'package:watoplan/localizations.dart';
import 'package:watoplan/data/models.dart';
import 'package:watoplan/intents.dart';
import 'package:watoplan/data/Provider.dart';
import 'package:watoplan/widgets/ActivityCard.dart';
import 'package:watoplan/widgets/FAM.dart';

class HomeScreen extends StatelessWidget {

  final String title;

  HomeScreen({Key key, this.title}) : super(key: key);

  List<MenuChoice> overflow = const <MenuChoice>[
    const MenuChoice(title: 'Settings', icon: Icons.settings, route: Routes.settings),
    const MenuChoice(title: 'About', icon: Icons.info, route: Routes.about)
  ];

  @override
  Widget build(BuildContext context) {
    var stateVal = Provider.of(context).value;

    List<SubFAB> typesToSubFABS(List<ActivityType> types) {
      return types.map(
        (it) => new SubFAB(
          icon: it.icon,
          color: it.color,
          onPressed: () {
            Intents.setFocused(Provider.of(context), -(types.indexOf(it) + 1));
            Navigator.of(context).pushNamed(Routes.addEditActivity);
          },
        )
      ).toList();
    }

    return new Scaffold(
      appBar: new AppBar(
        centerTitle: true,
        title: new Text(title ?? WatoplanLocalizations.of(context).appTitle),
        actions: <Widget>[
          new PopupMenuButton<MenuChoice>(
            onSelected: (MenuChoice choice) {
              Navigator.of(context).pushNamed(choice.route);
            },
            itemBuilder: (BuildContext context) =>
              overflow.map((MenuChoice choice) =>
                new PopupMenuItem<MenuChoice>(
                value: choice,
                child: new Row(
                  children: <Widget>[
                    new Icon(choice.icon),
                    new Padding(padding: new EdgeInsets.symmetric(horizontal: 8.0),),
                    new Text(choice.title)
                  ],
                ),
                )
              ).toList(),
          ),
        ],
      ),
      body: new ListView(
        padding: new EdgeInsets.symmetric(horizontal: 6.0, vertical: 6.0),
        children: new Iterable.generate(stateVal.activities.length)
          .map((it) => new ActivityCard(it)).toList()
      ),
      floatingActionButton: new FloatingActionMenu(
        color: Colors.amber,
        width: 56.0,
        height: 70.0,
        entries: typesToSubFABS(stateVal.activityTypes),
      ),
    );
  }
}
