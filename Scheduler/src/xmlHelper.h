#ifndef SCHEDULER_XML_HELPER_H
#define SCHEDULER_XML_HELPER_H

#include <iostream>
#include <string>

#include <auto_ptr.h>

#include "tinyxml.h"

#include "contract.h"

namespace Scheduler
{
  class CXmlHelper
  {
  public:
    std::auto_ptr<CContract> ReadContract(const std::string& xmlString);
  };
}

#endif // SCHEDULER_XML_HELPER_H