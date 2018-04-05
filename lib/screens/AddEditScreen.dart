import 'package:flutter/material.dart';

import 'package:watoplan/intents.dart';
import 'package:watoplan/localizations.dart';
import 'package:watoplan/data/models.dart';
import 'package:watoplan/data/Provider.dart';
import 'package:watoplan/widgets/ActivityDataInput.dart';

class AddEditScreen extends StatefulWidget {

  @override
  State<AddEditScreen> createState() => new AddEditScreenState();
}

class AddEditScreenState extends State<AddEditScreen> {

  @override
  Widget build(BuildContext context) {
    var stateVal = Provider.of(context).value;
    print(stateVal.focused.toString());
    Activity tmpActivity = stateVal.focused >= 0
        ? Activity.from(stateVal.activities[stateVal.focused])
        : new Activity(type: stateVal.activityTypes[-(stateVal.focused + 1)], data: {});

    List<ActivityDataInput> stringInputs = tmpActivity.data.
    
    return new Scaffold(
      appBar: new AppBar(
        backgroundColor: tmpActivity.data['color'],
        leading: new BackButton(),
        title: new Text(stateVal.focused >= 0
            ? stateVal.activities[stateVal.focused].data['name']
            : WatoplanLocalizations.of(context).newActivity),
      ),
      body: new Padding(
        padding: EdgeInsets.all(8.0),
        child: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[

            new ActivityDataInput(
              activity: tmpActivity,
              field: 'name',
            ),
            new ActivityDataInput(
              activity: tmpActivity,
              field: 'desc',
            )
          ],
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        child: new Icon(Icons.save),
        backgroundColor: tmpActivity.data['color'],
        onPressed: () {
          Intents.setFocused(Provider.of(context), stateVal.focused + 1);
        },
      ),
    );
  }
}
