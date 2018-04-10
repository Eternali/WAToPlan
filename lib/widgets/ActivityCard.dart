import 'package:flutter/material.dart';

import 'package:watoplan/routes.dart';
import 'package:watoplan/intents.dart';
import 'package:watoplan/data/models.dart';
import 'package:watoplan/data/Provider.dart';

class ActivityCard extends StatelessWidget {

  final Activity activity;
  ActivityCard(this.activity);

  @override
  Widget build(BuildContext context) {

    return new Card(
      color: activity.type.color,
      elevation: 6.0,
      child: new ListTile(
        leading: new Icon(activity.type.icon),
        isThreeLine: true,
        title: new Text(activity.data['name']),
        subtitle: new Text(activity.data['desc']),
        // trailing: new Icon(Icons.check),
        onTap: () {
          Intents.setFocused(Provider.of(context), activity: activity);
          Navigator.of(context).pushNamed(Routes.addEditActivity);
        },
      ),
    );
  }

}