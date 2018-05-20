import 'package:flutter/material.dart';

import 'package:watoplan/data/models.dart';
import 'package:watoplan/data/provider.dart';
import 'package:watoplan/intents.dart';
import 'package:watoplan/routes.dart';

class ActivityTypeCard extends StatelessWidget {

  final ActivityType data;
  final int indice;

  ActivityTypeCard(this.data, this.indice);

  @override
  Widget build(BuildContext context) {
    return new InkWell(
      child: new Card(
        color: data.color,
        child: new ListTile(
          leading: new Icon(data.icon),
          dense: true,
          title: new Text(
            data.name,
            style: new TextStyle(
              fontSize: 18.0
            ),
          ),
          subtitle: new Text(
            data.params.keys.join(', '),
            style: new TextStyle(
              fontSize: 12.0
            ),
          ),
          onTap: () {
            Intents.setFocused(Provider.of(context), indice: indice);
            Intents.editEditing(Provider.of(context), new ActivityType.from(data));
            Navigator.of(context).pushNamed(Routes.addEditActivityType);
          },
          trailing: new IconButton(
            padding: const EdgeInsets.all(0.0),
            alignment: Alignment.centerRight,
            icon: new Icon(Icons.clear),
            onPressed: () {
              Intents.removeActivityTypes(Provider.of(context), [data]);
            },
          ),
        )
      ),
    );
  }

}

