#! /usr/bin/env python
# -*- coding: utf-8 -*-

import fnmatch
import os

total_files = [file for file in os.listdir('.') if fnmatch.fnmatch(file, '*.textile')]
translated_files = [file for file in total_files if "Esta página todavía no ha sido traducida al castellano" not in open(file).read()]

total_size = sum([os.path.getsize(file) for file in total_files]) / 1000
translated_size = sum([os.path.getsize(file) for file in translated_files]) / 1000
translated_percent= translated_size * 100 / total_size

print "translated size: %dkb/%dkb %d%% (pending %dkb %d%%)" % \
      (translated_size, total_size, translated_percent, total_size-translated_size, 100-translated_percent)

total_count=len(total_files)
translated_count=len(translated_files)
translated_percent= translated_count * 100 / total_count

print "translated files: %d/%d %d%% (pending %d %d%%)" % \
      (translated_count, total_count, translated_percent, total_count-translated_count, 100-translated_percent)
