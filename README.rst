Username authenticator that checks for existing subject
=======================================================

.. image:: https://img.shields.io/badge/quality-beta-yellow
    :target: https://curity.io/resources/code-examples/status/

.. image:: https://img.shields.io/badge/availability-binary-blue
    :target: https://curity.io/resources/code-examples/status/

This authenticator is a clone of https://github.com/curityio/username-authenticator

Other than using a different authenticator type (`restricted-username`), this authenticator checks that the username exists using a configured account manager. 

If not, an error is shown to the user and they are required to submit a different username.
